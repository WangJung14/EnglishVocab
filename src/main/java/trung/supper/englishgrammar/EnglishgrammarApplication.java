package trung.supper.englishgrammar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class EnglishgrammarApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnglishgrammarApplication.class, args);
	}


    @RestController
    class HellController{
        @GetMapping("/hello")
        public String hello(){
            return "Hello World!";
        }
    }
}
