package com.reache.jeemanage.modules.act.rest.diagram.services;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class ProcessDefinitionDiagramLayoutResource extends BaseProcessDefinitionDiagramLayoutResource {
	
	@RequiresUser
	@RequestMapping(value = "/act/service/process-definition/{processDefinitionId}/diagram-layout", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getDiagram(@PathVariable String processDefinitionId) {
		return getDiagramNode(null, processDefinitionId);
	}
	
}
