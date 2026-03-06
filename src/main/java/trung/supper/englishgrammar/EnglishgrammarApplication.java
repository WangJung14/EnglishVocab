package trung.supper.englishgrammar;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class EnglishgrammarApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnglishgrammarApplication.class, args);
	}

}
