package com.tribloom.webscript;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.rendition.executer.AbstractRenderingEngine;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.rendition.RenditionDefinition;
import org.alfresco.service.cmr.rendition.RenditionService;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.http.HttpStatus;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

/**
 * Webscript controller that retrieves/generates a document rendition.
 * 
 * @author cpaul@tribloom.com
 */
public class GetXKCDArticle extends AbstractWebScript {

	// Rendition name
	private static final QName XKCD_RENDITION_DEF = QName.createQName("http://www.tribloom.com/model/1.0", "xkcdArticle");
	
	// Renderer name
	private static final String XKCD_RENDERER_NAME = "xkcd.renderer";

	// Injected by Spring
	private ServiceRegistry registry;
	public void setServiceRegistry(ServiceRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void execute(WebScriptRequest req, WebScriptResponse res)
			throws IOException {
		String nodeRefStr = req.getParameter("nodeRef");
		if (nodeRefStr == null) {
			error(res, HttpStatus.SC_NOT_FOUND, "Node not found");
			return;
		}
		NodeRef nodeRef = new NodeRef(nodeRefStr);
		RenditionService renditionService = registry.getRenditionService();
		
		// If a rendition already exists for this node, return it
		List<ChildAssociationRef> renditions = renditionService.getRenditions(nodeRef, MimetypeMap.MIMETYPE_TEXT_PLAIN);
		for (ChildAssociationRef assoc : renditions) {
			if (assoc.getQName().equals(XKCD_RENDITION_DEF)) {
				stream(res, assoc.getChildRef());
				return;
			}
		}

		// Retrieve the rendition definition
		RenditionDefinition action = renditionService.loadRenditionDefinition(XKCD_RENDITION_DEF);
		if (action == null) {
			// Create the definition if it hasn't already been created
			action = renditionService.createRenditionDefinition(XKCD_RENDITION_DEF, XKCD_RENDERER_NAME);
			renditionService.saveRenditionDefinition(action);
		}
		// Ensure the rendition is rebuilt if the source changes
		action.setParameterValue(AbstractRenderingEngine.PARAM_UPDATE_RENDITIONS_ON_ANY_PROPERTY_CHANGE, true);

		// Create the rendition and stream it back
		ChildAssociationRef renditionAssoc = renditionService.render(nodeRef, action);
		stream(res, renditionAssoc.getChildRef());
	}

	/**
	 * Helper method to stream back an error status and message.
	 * 
	 * @param res WebScriptResponse
	 * @param status int HTTP status code
	 * @param message String message
	 */
	private void error(WebScriptResponse res, int status, String message) {
		res.setStatus(status);
		res.setContentType("text/html");
		byte[] htmlBytes = ("<html><body><h1>" + message + "</h1></body></html>")
				.getBytes(Charset.forName("UTF-8"));
		try {
			res.getOutputStream().write(htmlBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper method to render the file as a download to the browser.
	 * 
	 * @param res WebScriptResponse
	 * @param nodeRef NodeRef to be downloaded
	 */
	private void stream(WebScriptResponse res, NodeRef nodeRef) {
		String filename = (String) registry.getNodeService().getProperty(nodeRef, ContentModel.PROP_NAME);
		try {
			ContentReader reader = registry.getContentService().getReader(nodeRef, ContentModel.PROP_CONTENT);
			res.setContentType(reader.getMimetype());
			res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			reader.getContent(res.getOutputStream());
		} catch (Exception ex) {
			throw new WebScriptException("Unable to stream output");
		}
	}
}
