
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.Properties;
import java.util.Set;

import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.jpa.dynamic.JPADynamicHelper;
import org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder;
import org.eclipse.persistence.sessions.Session;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.defs.NotNullDef;
import org.hibernate.validator.cfg.defs.SizeDef;

import ru.prime.app.validator.Validator;

import org.hibernate.validator.cfg.defs.DecimalMinDef;
import org.hibernate.validator.cfg.defs.DecimalMaxDef;
import org.hibernate.validator.cfg.defs.PatternDef;

import subpack.GenericEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Validation;
//import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.dynamic.DynamicEntity;


class LocalClass{
	int Id;
	String Value;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		this.Id = id;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		this.Value = value;
	}
	
	
	
}


public class Main{

	public void run() throws IOException{
	    Properties properties = new Properties();
	    properties.put(PersistenceUnitProperties.WEAVING, "static");
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("TABLE1",properties);
		EntityManager em = factory.createEntityManager();
	    Session session = JpaHelper.getEntityManager(em).getServerSession();
		DynamicClassLoader dynamicClassLoader = DynamicClassLoader.lookup(session);	    
	    JPADynamicHelper jpaDynamicHelper = new JPADynamicHelper(em);
	    Class<?> dynamicClass = dynamicClassLoader.createDynamicClass("DirectoryEntity");
		JPADynamicTypeBuilder typeBuilder = new JPADynamicTypeBuilder(dynamicClass,null,"TABLE2");
		typeBuilder.setPrimaryKeyFields("COLUMN1");
		typeBuilder.addDirectMapping("COLUMN1", Integer.class, "COLUMN1");
		typeBuilder.addDirectMapping("COLUMN2", String.class, "COLUMN2");
		typeBuilder.addDirectMapping("COLUMN3", String.class, "COLUMN3");
		typeBuilder.addDirectMapping("COLUMN4", String.class, "COLUMN4");
		
		
		
		
//	Validation introduction
		HibernateValidatorConfiguration configuration = Validation
		        .byProvider( HibernateValidator.class )
		        .configure();
		ConstraintMapping constraintMapping = configuration.createConstraintMapping();
		
		ElementType FIELD = ElementType.FIELD;
		constraintMapping
	    .type( dynamicClass )
	        .property( "COLUMN1", FIELD  )
	            .constraint( new NotNullDef() )
	            .constraint(new DecimalMinDef().value("10"))
	            .constraint(new DecimalMaxDef().value("30"))
	        .property( "COLUMN2", FIELD )
	            .constraint( new NotNullDef() )
	            .constraint( new PatternDef().regexp("[a-z]*"));

		FileInputStream in = new FileInputStream(new File("validationRule.xml"));
		javax.validation.Validator validator2 = configuration.addMapping( constraintMapping )
		        .buildValidatorFactory()
		        .getValidator();
		//configuration.addMapping(in);
		
		ValidatorFactory validationFactory = configuration.buildValidatorFactory();
		javax.validation.Validator validator = validationFactory.getValidator();

		
		
	    jpaDynamicHelper.addTypes(true, true, typeBuilder.getType());
	    //Actual code
	    DynamicEntity newDynamicEntity = jpaDynamicHelper.newDynamicEntity("DirectoryEntity");
	    newDynamicEntity.set("COLUMN1", 13);
	    newDynamicEntity.set("COLUMN2", "Column2 data");
	    newDynamicEntity.set("COLUMN3", "Column3 data");
	    newDynamicEntity.set("COLUMN4", "Column4 data");
	    
	    
	    GenericEntity ge = new GenericEntity();
	    ge.setCOLUMN1(15);
	    ge.setCOLUMN2("Hello");
	    ge.setCOLUMN3("Hello");
	    ge.setCOLUMN4("Hello");
	    
	    //Validation of static
	    Set<ConstraintViolation<GenericEntity>> constraintViolations;
	    constraintViolations = validator.validate(ge);
	    if(constraintViolations.size() > 0){
	    	System.out.println("Violation found");
	    	System.out.println(constraintViolations.iterator().next().getMessage());
	    }else{
	    	System.out.println("No static violation found");
	    }
	    //Validation of dynamic
	    System.out.println("========== Border between two comparisons");

	    Set<ConstraintViolation<DynamicEntity>> constraintViolationsDynamic;
	    constraintViolationsDynamic = validator2.validate(newDynamicEntity);
	    if(constraintViolationsDynamic.size() > 0){
	    	System.out.println("Dynamic violation found");
	    	System.out.println(constraintViolationsDynamic.iterator().next().getMessage());
	    }else{
	    	System.out.println("No dynamic violation found");
	    }
	    //Actual performing of persistence
	    /*em.getTransaction().begin();	    		
		em.persist(newDynamicEntity);
		em.getTransaction().commit();
		em.close(); */
	}

    public static void main(String[] args){
    	System.out.println("EclipseLink test");
/*    	Main app = new Main();
    	try{
    		app.run();
    	}catch(IOException ex){
    		ex.printStackTrace();
    	}*/
    	Validator val = new Validator();
    	val.setFileName("SimpleValidationRule.xml");
    	val.run();
    }

}
