package br.edu.ifpb.ifopendoors.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.edu.ifpb.ifopendoors.dao.DoorDAO;
import br.edu.ifpb.ifopendoors.dao.OpenDAO;
import br.edu.ifpb.ifopendoors.dao.PersonDAO;
import br.edu.ifpb.ifopendoors.dao.RoomDAO;
import br.edu.ifpb.ifopendoors.entity.Close;
import br.edu.ifpb.ifopendoors.entity.Door;
import br.edu.ifpb.ifopendoors.entity.Erro;
import br.edu.ifpb.ifopendoors.entity.Open;
import br.edu.ifpb.ifopendoors.entity.Person;
import br.edu.ifpb.ifopendoors.entity.Room;
import br.edu.ifpb.ifopendoors.exception.SQLExceptionIFOpenDoors;
import br.edu.ifpb.ifopendoors.util.BancoUtil;
import br.edu.ifpb.ifopendoors.validatio.Validate;

/**
 * Controlador para Room.
 *
 * @author Rhavy Maia
 * @version 1.0
 */
@Path("room")
public class RoomController {

	private static Logger logger = LogManager.getLogger(RoomController.class);
	
	/**
	 * Abertura da porta.
	 * 
	 * @param {?}
	 * @return
	 */
	@POST
	@Path("/open")
	@Consumes("application/json")
	@Produces("application/json")
	public Response open(Open open) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		// Validação dos dados de entrada.
		int validacao = Validate.open(open);
		
		
		if (validacao == Validate.VALIDATE_OK) {
			
			// Consultar Room e Person.
			Integer idPerson = open.getPerson().getId();
			Person person = PersonDAO.getInstance().getById(idPerson);	

			Integer idRoom = open.getRoom().getId();
			Room room = RoomDAO.getInstance().getById(idRoom);
			
			Integer idDoor = room.getDoor().getId();
			Door door = DoorDAO.getInstance().getById(idDoor);
			
			// Verificar disponibilidade de sala.
			//OpenDAO.getInstance().isOpenRoom(idRoom);
			
			if (person != null && room != null && door.getIp() != null) {
				
				open.setPerson(person);
				room.setDoor(door);
				open.setRoom(room);
				open.setTime("12/03");
				
				Integer idOpen = OpenDAO.getInstance().insert(open);
				
				if (idOpen != BancoUtil.IDVAZIO) {
					
					Client client = ClientBuilder.newClient();
		    		Response response = client.target("htttp://" + open.getRoom().getDoor().getIp() + "/open")
		    				.request().get();
	
					builder.status(Response.Status.OK);
					builder.entity(open);
				}
			}
		}		
		
		return builder.build();		
	}
	
	/**
	 * Abertura da porta.
	 * 
	 * @param {?}
	 * @return
	 */
	@POST
	@Path("/close")
	@Consumes("application/json")
	@Produces("application/json")
	public Response close(Close open) {
		return null;		
	}
	
	@GET
	@Path("/all")
	@Produces("application/json")
	public List<Room> getAll() {
		
		List<Room> roons = new ArrayList<Room>();
		
		roons = RoomDAO.getInstance().getAll();
		
		return roons;
	}
	
	@POST
	@Path("/insert")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insertRoom(Room room) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		try {
			
			Integer idRoom = RoomDAO.getInstance().insert(room);
			
			if (idRoom != BancoUtil.IDVAZIO) {

				builder.status(Response.Status.OK);
				builder.entity(room);
			}
		
		} catch (SQLExceptionIFOpenDoors qme) {
			
			Erro erro = new Erro();
			erro.setCodigo(qme.getErrorCode());
			erro.setMensagem(qme.getMessage());

			builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro);
			
		}		
		
		return builder.build();		
	}
	
	
	@GET
	@Path("/id/{id}")
	@Produces("application/json")
	public Response getRoomById(@PathParam("id") int idRoom) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());

		try {

			Room room = RoomDAO.getInstance().getById(idRoom); 
			
			builder.status(Response.Status.OK);
			builder.entity(room);

		} catch (SQLExceptionIFOpenDoors qme) {

			Erro erro = new Erro();
			erro.setCodigo(qme.getErrorCode());
			erro.setMensagem(qme.getMessage());

			builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro);
		}

		return builder.build();
	}
	
	
	
	@GET
	@Path("/entity")
	@Produces("application/json")
	public Response getEntity() {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		Open open = new Open();
		
		Room room = RoomDAO.getInstance().getById(1); 
		
		Person person = PersonDAO.getInstance().getById(1);
		
		open.setRoom(room);
		open.setPerson(person);
		
		builder.status(Response.Status.OK).entity(open); 
		
		return builder.build();		
	}
}
