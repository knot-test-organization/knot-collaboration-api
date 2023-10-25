package com.nttdata.knot.collaborationapi.Models.Collaboration;

import java.util.List;


import com.nttdata.knot.collaborationapi.Models.UserPackage.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MicrosoftTeams {
    private Boolean enabled;
    private String name;
    private String description;
    private List<User> usersList;

    public MicrosoftTeams(Boolean enabled, String name, String description, List<User> usersList) {
        this.enabled = enabled;
        this.name = name;
        this.description = description;
        this.usersList = usersList;
    }
}