package pet.park.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.park.Dao.ContributorDao;
import pet.park.controller.mode.ContributorData;
import pet.park.entity.Contributor;

@Service
public class ParkService {
	
	@Autowired
	private ContributorDao contributorDao;

	@Transactional(readOnly = false)
	public ContributorData saveContributor(ContributorData contributorData) {
		Long contributorId = contributorData.getContributorId();
		Contributor contributor = findOrCreateContributor(contributorId);
		
		setFieldsInContributor(contributor, contributorData);
		return new ContributorData(contributorDao.save(contributor));
	}
	
	private void setFieldsInContributor(Contributor contributor, 
			ContributorData contributorData) {
		
		contributor.setContributorEmail(contributorData.getContributorEmail());
		contributor.setContributorName(contributorData.getContributorName());
	}

	private Contributor findOrCreateContributor(Long contributorId) {
		Contributor contributor;
		
		if(Objects.isNull(contributorId)) {
			contributor = new Contributor();
		}
		else {
			contributor = findContributorById(contributorId);
		}
		
		return contributor;
		
	
	}

	private Contributor findContributorById(Long contributorId) {
		return contributorDao.findById(contributorId)
	       .orElseThrow(() -> new NoSuchElementException(
	    	"Contributor with ID=" + contributorId + "was not found."));
	}

	@Transactional(readOnly = true)
	public List<ContributorData> retrieveAllContributors() {
		
		//method enhanced for loop 
//		List<Contributor> contributors = contributorDao.findAll();
//		List<ContributorData> response = new LinkedList<>();
//		
//		for (Contributor contributor : contributors) {
//			response.add(new ContributorData(contributor));
//			
//		}
//		
//		return response;
		
		//nuisance Java 8th stream 
		//@formatter:off
		return contributorDao.findAll()
		    .stream()
		    .map(ContributorData::new)
		    .toList();
		//@formatter:on
	}

	public ContributorData retrieveContributorById(Long contributorId) {
		Contributor contributor = findContributorById(contributorId);
		return new ContributorData(contributor);
	}
	

}
