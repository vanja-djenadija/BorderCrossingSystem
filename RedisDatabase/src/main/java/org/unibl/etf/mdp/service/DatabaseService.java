package org.unibl.etf.mdp.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.unibl.etf.mdp.model.User;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Path("/users")
public class DatabaseService {

	private static final String HOST = "localhost";

	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyCredentials(@PathParam("username") String username) {
		try (JedisPool pool = new JedisPool(HOST); Jedis jedis = pool.getResource()) {
			if (jedis.exists(username)) {
				String password = jedis.get(username);
				return Response.status(Response.Status.OK).entity(new User(username, password)).build();
			} else
				return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUser(User user) {
		try (JedisPool pool = new JedisPool(HOST); Jedis jedis = pool.getResource()) {
			if (jedis.exists(user.getUsername())) {
				return Response.status(Response.Status.CONFLICT).build();
			} else {
				jedis.set(user.getUsername(), user.getPassword());
				return Response.status(Response.Status.OK).entity(user).build();
			}
		}
	}

	@DELETE
	@Path("/{username}")
	public Response deleteUser(@PathParam("username") String username) {
		try (JedisPool pool = new JedisPool(HOST); Jedis jedis = pool.getResource()) {
			if (!jedis.exists(username)) {
				return Response.status(Response.Status.NOT_FOUND).build();
			} else {
				jedis.del(username);
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(User user) {
		try (JedisPool pool = new JedisPool(HOST); Jedis jedis = pool.getResource()) {
			if (!jedis.exists(user.getUsername())) {
				return Response.status(Response.Status.NOT_FOUND).build();
			} else {
				jedis.set(user.getUsername(), user.getPassword());
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
	}

}
