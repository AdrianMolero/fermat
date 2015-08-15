package com.bitdubai.fermat_api.layer.all_definition.resources_structure;

import com.bitdubai.fermat_api.layer.all_definition.resources_structure.enums.*;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.interfaces.FermatResource;

import java.io.Serializable;
import java.util.UUID;



/**
 * The Class <code>com.bitdubai.fermat_api.layer.all_definition.resources_structure.Resource</code>
 * implements the functionality of a Fermat Resource.
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/15.
 * @version 1.0
 * @since Java JDK 1.7
*/
public class Resource implements Serializable {

    /**
     * Resource Class private attributes
     */
    private UUID id;

    private String name;

    private String fileName;

    private ResourceType resourceType;

    private ResourceDensity resourceDensity;


    /**
     * Resource Class Constructors
     */
    public Resource() {
    }

    public Resource(String name, String fileName, ResourceType resourceType) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.fileName = fileName;
        this.resourceType = resourceType;
    }

    public Resource(String name, String fileName, ResourceType resourceType, ResourceDensity resourceDensity) {
        this.name = name;
        this.fileName = fileName;
        this.resourceType = resourceType;
        this.resourceDensity = resourceDensity;
    }

    public Resource(UUID id, String name, String fileName, ResourceType resourceType,ResourceDensity resourceDensity) {
        this.id = id;
        this.name = name;
        this.fileName = fileName;
        this.resourceType = resourceType;
        this.resourceDensity=resourceDensity;
    }



    /**
     * Resource Class getters
     */
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }


    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Resource Class setters
     */
    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }
}
