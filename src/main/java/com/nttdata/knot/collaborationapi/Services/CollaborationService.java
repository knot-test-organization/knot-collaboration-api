package com.nttdata.knot.collaborationapi.Services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.nttdata.knot.collaborationapi.Models.Collaboration.Collaboration;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.nttdata.knot.collaborationapi.Interfaces.ICollaborationService;
import com.nttdata.knot.collaborationapi.Interfaces.IGithubService;
import com.nttdata.knot.collaborationapi.Models.ComponentPackage.Component;
import com.nttdata.knot.collaborationapi.Models.GithubPackage.GithubFileRequest.Committer;
import com.nttdata.knot.collaborationapi.Models.GithubPackage.GithubFileRequest.CreateGithubFileRequest;
import com.nttdata.knot.collaborationapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;

import reactor.core.publisher.Mono;

@Service
public class CollaborationService implements ICollaborationService {

    private String repoName = "knot-onboarding-resources";
    private IGithubService githubService;

    public CollaborationService(IGithubService githubService) {
        this.githubService = githubService;
    }

    @Override
    public Mono<Collaboration> createCollaborationAsync(String org, String area, String product, Component component) throws JsonProcessingException {

        // Populate the collaboration object

        Collaboration collaboration = new Collaboration();
        collaboration.setName(component.getId());
        collaboration.setMicrosoftTeams(component.getMicrosoftTeams());

        // prepare the verticals values commit
        var values_collaboration = prepareValueForCommit(component, collaboration);

        // push the values of each vertical in knot-onboarding-resources
        this.githubService.createGithubFileAsync(values_collaboration, repoName,
                "products/" + org + "/" + area + "/" + product + "/" + component.getId() + "/collaboration/values.yaml").block();

        return Mono.just(collaboration);
    }

    @Override
    public Mono<DeleteGithubFileRequest> deleteCollaborationAsync(String org, String area, String product, String deletedComponentName) {

        // get the file to delete
        var valuesFile = this.githubService.getGithubFileAsync(repoName,
        "products/" + org + "/" + area + "/" + product + "/" + deletedComponentName + "/collaboration/values.yaml").block();

        // set the commit
        Committer committer = new Committer();
        committer.setEmail("41898282+github-actions[bot]@users.noreply.github.com");
        committer.setName("github-actions[bot]");

        DeleteGithubFileRequest deleteGithubFileRequest = new DeleteGithubFileRequest();
        deleteGithubFileRequest.setMessage("Removing Collaboration vertical into a Component, with name " + deletedComponentName);
        deleteGithubFileRequest.setCommitter(committer);
        deleteGithubFileRequest.setSha(valuesFile.getSha());

        // delete the file
        this.githubService.deleteGithubFileAsync(deleteGithubFileRequest,
                repoName,
                "products/" + org + "/" + area + "/" + product + "/" + deletedComponentName + "/collaboration/values.yaml").block();

        return Mono.just(deleteGithubFileRequest);
    }

    @Override
    public Mono<Collaboration> updateCollaborationAsync(String org, String area, String product, Component component) throws JsonProcessingException {      
    
        // Populate the collaboration object
        Collaboration collaboration = new Collaboration();
        collaboration.setName(component.getId());
        collaboration.setMicrosoftTeams(component.getMicrosoftTeams());
        
        // get the values file to update
        var valuesFile = this.githubService.getGithubFileAsync(repoName,
        "products/" + org + "/" + area + "/" + product + "/" + component.getId() + "/collaboration/values.yaml").block();  

        // prepare the verticals values commit
        var values_collaboration = prepareValueForCommit(component, collaboration);
        values_collaboration.setSha(valuesFile.getSha());     
        
        // push the values to the repository
        this.githubService.createGithubFileAsync(values_collaboration, repoName,
                "products/" + org + "/" + area + "/" + product + "/" + component.getId() + "/collaboration/values.yaml").block();

        return Mono.just(collaboration);
    }

    // serialize content of values and prepare a commit
    private CreateGithubFileRequest prepareValueForCommit(Component component, Object vertical)
            throws JsonProcessingException {
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);

        String verticalInBase64String = Base64.getEncoder()
                .encodeToString(objectMapper
                        .writeValueAsString(vertical).getBytes(StandardCharsets.UTF_8));

        Committer committer = new Committer();
        committer.setEmail("41898282+github-actions[bot]@users.noreply.github.com");
        committer.setName("github-actions[bot]");

        CreateGithubFileRequest createGithubFileRequest = new CreateGithubFileRequest();
        createGithubFileRequest.setMessage("Add new Collaboration vertical into a Component, with name " + component.getName());
        createGithubFileRequest.setCommitter(committer);
        createGithubFileRequest.setContent(verticalInBase64String);

        return createGithubFileRequest;

    }
}
