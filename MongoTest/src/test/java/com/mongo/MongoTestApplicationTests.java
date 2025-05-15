package com.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mongo.entity.Article;
import com.mongo.entity.Details;
import com.mongo.entity.Dimensions;
import com.mongo.repository.ArticlesRepository;

@SpringBootTest
class MongoTestApplicationTests {

	private static final EasyRandom ER = new EasyRandom();
	
	@Autowired
	ArticlesRepository articlesRepository;
	
	@Test
	void personalisedMethods() {
		
		articlesRepository.findByName("NAME!").forEach(System.out::println);
		
		articlesRepository.findByDimensionsHeight(888).forEach(System.out::println);
		
		articlesRepository.findByDetailsValue("qszYL").forEach(System.out::println);
		
		//articlesRepository.customQuery("NAME!", 666, 8).forEach(System.out::println);
	}
	
	@Test
	void insertSpecific() {
		Dimensions d = new Dimensions();
		d.setHeight(10);
		d.setWidth(8);
		d.setDepth(50);
		
		Details de = new Details();
		de.setValue("detailValue");
		
		List<Details> details = new ArrayList<>();
		details.add(de);
		
		Article a = new Article();
		a.setName("NAME!");
		a.setDimensions(d);
		a.setDetails(details);
		
		System.out.println(articlesRepository.count());
		System.out.println(articlesRepository.save(a));
		System.out.println(articlesRepository.count());
	}
	
	@Test
	void insertMany() {
		
		
		List<Article> articles = ER.objects(Article.class, 10).collect(Collectors.toList());
		articles.stream().forEach(article -> article.setId(null));
		
		System.out.println(articlesRepository.count());
		System.out.println(articlesRepository.saveAll(articles));
		System.out.println(articlesRepository.count());

		//the above ER generates random value and a random number of values hence why some Articles have countless value/details as attributes has a list of details of unspecified size.
	}
	
	@Test
	//updates any entries that were void of dimensions to 888 height and any others to 666 height.
	void update() {
		List<Article> allArticles = articlesRepository.findAll();
		allArticles.stream().forEach(System.out::println);
		
		for(Article article : allArticles) {
			if(article.getDimensions() != null) {
				article.getDimensions().setHeight(666);
			} else {
				Dimensions d = new Dimensions();
				d.setHeight(888);
				article.setDimensions(d);
			}
		}
		
		//allArticles.get(3).getDimensions().setHeight(666);
		articlesRepository.saveAll(allArticles);
	}
	
	@Test
	void deleteArticleById() {
		
		String id = "eOMtThyhVNLWUZNRcBaQKxI";
		articlesRepository.deleteById(id);
	}
	
	@Test
	void deleteByArticle() {
		
		List<Article> all = articlesRepository.findAll();
		articlesRepository.delete(all.get(0));
	}

}












