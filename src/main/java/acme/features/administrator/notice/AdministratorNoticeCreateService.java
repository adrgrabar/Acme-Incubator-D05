
package acme.features.administrator.notice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.notices.Notice;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractCreateService;

@Service
public class AdministratorNoticeCreateService implements AbstractCreateService<Administrator, Notice> {

	@Autowired
	AdministratorNoticeRepository repository;


	@Override
	public boolean authorise(final Request<Notice> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Notice> request, final Notice entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "date");
	}

	@Override
	public void unbind(final Request<Notice> request, final Notice entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "picture", "deadline", "body", "links");
	}

	@Override
	public Notice instantiate(final Request<Notice> request) {
		Notice result;
		result = new Notice();
		return result;
	}

	@Override
	public void validate(final Request<Notice> request, final Notice entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		if (!errors.hasErrors("confirmation")) {
			errors.state(request, request.getModel().getString("confirmation").equals("true"), "confirmation", "administrator.notice.error.confirmation");
		}
		if (!errors.hasErrors("deadline")) {
			errors.state(request, entity.getDeadline().after(new Date(System.currentTimeMillis())), "deadline", "administrator.notice.error.deadline");
		}
	}

	@Override
	public void create(final Request<Notice> request, final Notice entity) {
		assert request != null;
		assert entity != null;
		Date date;
		date = new Date(System.currentTimeMillis() - 1);
		entity.setDate(date);
		this.repository.save(entity);
	}
}
