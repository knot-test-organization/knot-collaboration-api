package com.nttdata.knot.collaborationapi.Controllers;



import com.nttdata.knot.collaborationapi.Models.Collaboration.Collaboration;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.collaborationapi.Interfaces.ICollaborationService;
import com.nttdata.knot.collaborationapi.Models.ComponentPackage.Component;
import com.nttdata.knot.collaborationapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;

@RestController
@RequestMapping("/collaboration")
public class collaborationController {

    private ICollaborationService collaborationService;
    private static final Logger logger = LoggerFactory.getLogger(Collaboration.class);

    @Autowired
    public collaborationController(ICollaborationService collaborationService) {
        this.collaborationService = collaborationService;
       
    }

    @PostMapping("/{org}/{area}/{product}")
    public  ResponseEntity<Mono<Collaboration>> create(@PathVariable String org, @PathVariable String area, @PathVariable String product, @RequestBody Component component) throws JsonProcessingException {
         var iac = collaborationService.createCollaborationAsync(org, area, product, component);
        return ResponseEntity.ok(iac);
    }
    
     @DeleteMapping("/{org}/{area}/{product}/{name}")
    public  ResponseEntity<Mono<DeleteGithubFileRequest>> delete(@PathVariable String org, @PathVariable String area, @PathVariable String product, @PathVariable String name) throws JsonProcessingException {
          
        var alm = collaborationService.deleteCollaborationAsync(org, area, product, name);
        logger.info("The component {} is being deleted", name);

        return ResponseEntity.ok(alm);
    }

    @PutMapping("/{org}/{area}/{product}")
    public  ResponseEntity<Mono<Collaboration>> update (@PathVariable String org, @PathVariable String area, @PathVariable String product, @RequestBody Component component) throws JsonProcessingException {
          
        var collaboration = collaborationService.updateCollaborationAsync(org, area, product, component);
        logger.info("The component {} is being Updated", component.getName());

        return ResponseEntity.ok(collaboration);
    }
  


}

