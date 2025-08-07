package com.abc.hr.app.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import static com.abc.hr.app.constants.HrEmployeeDataExtractorConstants.*;
import com.abc.hr.app.entity.Employee;
import com.abc.hr.app.entity.EmployeeDetails;
import com.abc.hr.app.listener.HrEmployeeDataExtractorJobCompletionNotificationListener;
import com.abc.hr.app.processor.EmployeeDataProcessor;
import com.abc.hr.app.reader.EmployeeDataReader;
import com.abc.hr.app.tasklet.CleanupOldCsvFilesTasklet;
import com.abc.hr.app.writer.EmployeeDataFileItemWriter;

@Configuration
@EnableBatchProcessing
public class HrEmployeeDataExtractorConfiguration {

    @Bean
	public EmployeeDataProcessor processor() {
		return new EmployeeDataProcessor();
	}
    
    @Bean
    public HrEmployeeDataExtractorJobCompletionNotificationListener listener() {
    	return new HrEmployeeDataExtractorJobCompletionNotificationListener();
    }
       
    @Bean
    public Job exportEmployeeJob(JobBuilderFactory jobBuilderFactory,
                                  Step extractEmployeeDataStep,
                                  Step cleanupOldCsvFilesStep) {

        return jobBuilderFactory.get(SERVICE_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .start(extractEmployeeDataStep) 
                .on("COMPLETED").to(cleanupOldCsvFilesStep)//Only proceed if extractEmployeeDataStep is successful
                .from(extractEmployeeDataStep).on("FAILED").fail() //Ends job for FAILED status
                .end()
                .build();
    }
     
    @Bean
    public Step extractEmployeeDataStep(StepBuilderFactory stepBuilderFactory,
			 EmployeeDataReader reader, EmployeeDataFileItemWriter writer) {

		return stepBuilderFactory.get(EXTRACT_DATA_STEP)
								 .<Employee,EmployeeDetails>chunk(20)
								 .reader(reader)
								 .processor(processor())
								 .writer(writer)
								 .build();
	}
   
   @Bean
   public Step cleanupOldCsvFilesStep(StepBuilderFactory stepBuilderFactory,
                                      CleanupOldCsvFilesTasklet cleanupTasklet) {
       return stepBuilderFactory.get(CLEANUP_OLD_CSVFILES_STEP_NAME)
               .tasklet(cleanupTasklet)
               .build();
   }

    
    /** This configuration is added to bypass the error: 
     * "unable to find valid certification path to requested target"  which occurs when making calls to public APIs.
    **/
   	@Bean
   	public RestTemplate unsafeRestTemplate() throws Exception {
   	    TrustManager[] trustAllCerts = new TrustManager[]{
   	        new X509TrustManager() {
   	            public void checkClientTrusted(X509Certificate[] xcs, String string) {}
   	            public void checkServerTrusted(X509Certificate[] xcs, String string) {}
   	            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
   	        }
   	    };
   	 
   	    SSLContext sslContext = SSLContext.getInstance("TLS");
   	    sslContext.init(null, trustAllCerts, new SecureRandom());
   	    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
   	    HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);
   	 
   	    return new RestTemplate(); 
   	} 
 }

