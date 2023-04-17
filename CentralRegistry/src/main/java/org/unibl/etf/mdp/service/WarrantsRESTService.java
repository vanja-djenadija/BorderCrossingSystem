package org.unibl.etf.mdp.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/warrants")
public class WarrantsRESTService {

	public static final String WARRANTS_PATH = "." + File.separator + "CentralRegistry" + File.separator + "resources"
			+ File.separator + "warrants.xml";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArrestWarants() {
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(WARRANTS_PATH), StandardCharsets.UTF_8);
			return Response.status(Response.Status.OK).entity(lines).build();
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}
