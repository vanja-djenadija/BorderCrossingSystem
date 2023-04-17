package org.unibl.etf.mdp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/documents")
public class RESTService {

	private static final String DOCUMENTS_PATH = "." + File.separator + "FileServer" + File.separator + "resources" + File.separator + "documents"; 
	@GET
	@Path("/{personId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocuments(@PathParam("personId") String personId) {
		String personFolder = DOCUMENTS_PATH + File.separator + personId;
		File file = new File(personFolder);
		if (!file.getAbsoluteFile().exists()) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		ArrayList<byte[]> documents = getDocuments(file.listFiles());
		return Response.status(Response.Status.OK).entity(documents).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocuments() {
		File rootFolder = new File(CustomsFileTransferService.DOCUMENTS_PATH);
		File[] folders = rootFolder.listFiles();
		if (folders.length == 0)
			return Response.status(Response.Status.NO_CONTENT).build();

		HashMap<String, ArrayList<byte[]>> documents = new HashMap<>();
		for (File folder : folders)
			documents.put(folder.getName(), getDocuments(folder.listFiles()));

		return Response.status(Response.Status.OK).entity(documents).build();
	}

	private ArrayList<byte[]> getDocuments(File[] files) {
		ArrayList<byte[]> documents = new ArrayList<>();
		for (File f : files) {
			try (FileInputStream fis = new FileInputStream(f)) {
				documents.add(fis.readAllBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return documents;
	}
}
