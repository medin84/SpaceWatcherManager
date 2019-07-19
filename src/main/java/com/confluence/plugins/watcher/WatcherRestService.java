package com.confluence.plugins.watcher;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.spring.container.ContainerManager;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/watcher")
@Produces(MediaType.APPLICATION_JSON)
@Scanned
public class WatcherRestService {

    @ComponentImport
    private ActiveObjects ao;
    private PermissionManager permissionManager;
    private SettingsService settingsService;

    public WatcherRestService(@ComponentImport PluginSettingsFactory psf, ActiveObjects ao) {
        permissionManager = (PermissionManager) ContainerManager.getComponent("permissionManager");
        settingsService = new SettingsService(psf);
        this.ao = ao;
    }

      // for dev only
//    @GET
//    @Path("/ao")
//    public Response deleteAO() {
//        WatcherUserEntity[] entities = ao.find(WatcherUserEntity.class);
//        for(WatcherUserEntity entity : entities){
//            ao.delete(entity);
//        }
//        return Response.ok("deleted").build();
//    }

    @GET
    @Path("/settings")
    public Response getSettings() {
        return Response.ok(settingsService.get()).build();
    }

    @POST
    @Path("/settings")
    public Response saveSettings(SettingsModel sm) {
        ConfluenceUser user = AuthenticatedUserThreadLocal.get();

        if (!permissionManager.isSystemAdministrator(user)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            settingsService.set(sm);
            return Response.ok(sm).build();
        } catch (SettingsValidateException e) {
            ErrorResponse response = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }
}
