package tn.esprit.seahawks.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.interfaces.BreedingOfferServiceLocal;
import tn.esprit.seahawks.interfaces.ReportServiceLocal;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.FoundReport;
import tn.esprit.seahawks.persistence.SendEmail;

@Stateless
public class TimerService {

	@EJB(beanName = "BreedingOfferService")
	BreedingOfferServiceLocal breedingService;

	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@EJB(beanName = "ReportService")
	ReportServiceLocal frs;

	// @Schedule(second="*/45", minute="*",hour="*", persistent=false)
	public void Close() {
		List<FoundReport> flist = frs.getOpenFoundReport();
		Iterator<FoundReport> itr = flist.iterator();
		while (itr.hasNext()) {
			FoundReport element = itr.next();
			frs.CloseFoundReport(frs.passToCareTaker(element), element);
			// InformViaMailOrganisation
			// InformViaMailReporter
		}

		// }

	}

}