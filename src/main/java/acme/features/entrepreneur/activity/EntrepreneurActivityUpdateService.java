
package acme.features.entrepreneur.activity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.investmentRounds.Activity;
import acme.entities.investmentRounds.InvestmentRound;
import acme.entities.roles.Entrepreneur;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractUpdateService;

@Service
public class EntrepreneurActivityUpdateService implements AbstractUpdateService<Entrepreneur, Activity> {

	@Autowired
	EntrepreneurActivityRepository repository;


	@Override
	public boolean authorise(final Request<Activity> request) {
		assert request != null;

		boolean result;
		InvestmentRound ir;
		Integer activityId;
		Activity activity;

		activityId = request.getModel().getInteger("id");
		activity = this.repository.findOneById(activityId);
		ir = activity.getInvestmentRound();

		Principal principal;
		Entrepreneur entrepreneur;

		principal = request.getPrincipal();
		entrepreneur = ir.getEntrepreneur();
		result = principal.getAccountId() == entrepreneur.getUserAccount().getId();

		return result && !ir.getPublished();
	}

	@Override
	public void bind(final Request<Activity> request, final Activity entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Activity> request, final Activity entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "startDate", "endDate", "budget");
	}

	@Override
	public Activity findOne(final Request<Activity> request) {
		Activity result;
		int id;
		id = request.getModel().getInteger("id");
		result = this.repository.findOneById(id);
		return result;
	}

	@Override
	public void validate(final Request<Activity> request, final Activity entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		if (!errors.hasErrors("startDate") && !errors.hasErrors("endDate")) {
			errors.state(request, entity.getStartDate().before(entity.getEndDate()), "endDate", "entrepreneur.activity.error.wrongDates");
		}
		if (!errors.hasErrors("budget")) {
			errors.state(request, entity.getBudget().getCurrency().equals("EUR") || entity.getBudget().getCurrency().equals("€"), "budget", "entrepreneur.activity.error.euro");
		}
		if (!errors.hasErrors("startDate")) {
			errors.state(request, entity.getStartDate().after(new Date(System.currentTimeMillis() - 1)), "startDate", "entrepreneur.activity.error.pastDate");
		}
		if (!errors.hasErrors("endDate")) {
			errors.state(request, entity.getEndDate().after(new Date(System.currentTimeMillis() - 1)), "endDate", "entrepreneur.activity.error.pastDate");
		}
	}

	@Override
	public void update(final Request<Activity> request, final Activity entity) {
		assert request != null;
		assert entity != null;
		this.repository.save(entity);
	}
}
