package com.example.demo.xfs;


import java.util.List;
import java.util.Map;

import com.example.demo.xfs.TeamBeanRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
//@CrossOrigin(origins="http://ec2-52-66-245-186.ap-south-1.compute.amazonaws.com:4200")
@RequestMapping("/api")
public class XfsController {  
	@Autowired
	private  MongoTemplate mongoTemplate; 
	
	
	@Autowired
	private  XfsRepository xfsRepository; 
	
	@Autowired
	private  TeamBeanRepository TeamBeanRepository; 
	
	public XfsController(XfsRepository xfsRepository, TeamBeanRepository TeamBeanRepository) {
		this.xfsRepository=xfsRepository;
		this.TeamBeanRepository=TeamBeanRepository;
	}
	
		
	
	
	@GetMapping("/xfs")
	public Xfs getXFS() {
	  List<Xfs> xfs = xfsRepository.findAll();
	  
	  return xfs.get(xfs.size() - 1);
	  
	}
	
	@GetMapping("/teamlist")
	public  List<TeamBean> getTeamBean() {
	  List<TeamBean> TeamBean = TeamBeanRepository.findAll();
	  
	  return TeamBean;
	  
	}
	
	@PostMapping("/addteam")
	public TeamBean addXFSData(@RequestBody TeamBean TeamBean) {
		TeamBean.set_id(ObjectId.get());
		TeamBeanRepository.save(TeamBean);
		return TeamBean;
	}
		

	@PostMapping("/addbuildinfo")
	public TeamBean addTeamBeanData(@RequestBody Map<String,Object> buildInfo) {
//		buildInfo.set_id(ObjectId.get());
		
		TeamBean p= mongoTemplate.findOne(
				  Query.query(Criteria.where("teamName").is((buildInfo).get("teamName"))), TeamBean.class);
		String gitUserName=p.gitUserName;
		String gitRepoName=p.gitRepoName;
		 String URL="https://api.github.com/repos/"+gitUserName+"/"+gitRepoName+"/commits";
		 System.out.println(URL);
		  RestTemplate restTemplate = new RestTemplate();
		      String result = restTemplate.getForObject(URL.toString(), String.class);
		      
		      
		      String arr[] = result.split(":");
		      System.out.println(arr.length);

		      String str=arr[5];
		      String arr2[] = str.split(",");
		 System.out.println(arr2[0]);

		     
		  
		p.bUrl= buildInfo.get("bUrl").toString();
		p.bNumber= buildInfo.get("bNumber").toString();
		p.buildStatus= buildInfo.get("buildStatus").toString();
		p.l_commit=arr2[0].replaceAll("^\"|\"$","");
		p.jobtitle= buildInfo.get("jobtitle").toString();
		
		TeamBeanRepository.save(p); 
		return p;
	}
}



		
   
	
	

