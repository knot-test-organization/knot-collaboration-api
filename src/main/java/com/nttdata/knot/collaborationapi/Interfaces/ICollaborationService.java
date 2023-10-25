package com.nttdata.knot.collaborationapi.Interfaces;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.collaborationapi.Models.Collaboration.Collaboration;
import com.nttdata.knot.collaborationapi.Models.ComponentPackage.Component;
import com.nttdata.knot.collaborationapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;

import reactor.core.publisher.Mono;


public interface ICollaborationService {
    

    Mono<Collaboration> createCollaborationAsync(String org, String area, String product, Component component) throws JsonProcessingException;

    Mono<DeleteGithubFileRequest> deleteCollaborationAsync(String org, String area, String product, String deletedComponentName);

    Mono<Collaboration> updateCollaborationAsync(String org, String area, String product, Component component) throws JsonProcessingException;


    
}
