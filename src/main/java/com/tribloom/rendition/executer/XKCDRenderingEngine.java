package com.tribloom.rendition.executer;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.rendition.executer.AbstractRenderingEngine;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;

public class XKCDRenderingEngine extends AbstractRenderingEngine {
	
	private static final String[][] substitutions = {
		{"WITNESSES", "THESE DUDES I KNOW"},
		{"ALLEGEDLY", "KINDA PROBABLY"},
		{"NEW STUDY", "TUMBLR POST"},
		{"REBUILD", "AVENGE"},
		{"SPACE", "SPAAACE"},
		{"GOOGLE GLASS", "VIRTUAL BOY"},
		{"SMARTPHONE", "POKEDEX"},
		{"ELECTRIC", "ATOMIC"},
		{"SENATOR", "ELF-LORD"},
		{"CAR", "CAT"},
		{"ELECTION", "EATING CONTEST"},
		{"CONGRESSIONAL LEADERS", "RIVER SPIRITS"},
		{"HOMELAND SECURITY", "HOMESTAR RUNNER"},
		{"COULD NOT BE REACHED FOR COMMENT", "IS GUILTY AND EVERYONE KNOWS IT"}
	};

	@Override
	protected void render(RenderingContext context) {
		ContentReader reader = contentService.getReader(context.getSourceNode(), ContentModel.PROP_CONTENT);
		ContentWriter writer = contentService.getWriter(context.getDestinationNode(), ContentModel.PROP_CONTENT, true);
		writer.setEncoding(reader.getEncoding());
		writer.setMimetype(reader.getMimetype());
		writer.putContent(xkcdSubstitutions(reader.getContentString()));
		String newName = "xkcd-" + (String) nodeService.getProperty(context.getSourceNode(), ContentModel.PROP_NAME);
		nodeService.setProperty(context.getDestinationNode(), ContentModel.PROP_NAME, newName);
	}
	
	public String xkcdSubstitutions(String content) {
		content = content.toUpperCase();
		for (int i = 0; i < substitutions.length; i++) {
			String[] match = substitutions[i];
			content = content.replaceAll(match[0], match[1]);
		}
		return content;
	}

}
