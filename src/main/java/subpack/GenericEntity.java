package subpack;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.persistence.internal.dynamic.DynamicEntityImpl;
import org.eclipse.persistence.internal.dynamic.DynamicPropertiesManager;


/* Hmm. Interesting reference
 * https://wiki.eclipse.org/EclipseLink/Examples/JPA/Dynamic/CustomizeAttributes#Step2_:_create_your_own_DynamicPropertiesManager
 * And a bit more
 * http://wiki.eclipse.org/EclipseLink/Development/Dynamic
 */


@Entity
public class GenericEntity  extends DynamicEntityImpl{
	
	public static DynamicPropertiesManager DPM = new DynamicPropertiesManager();
	
	@Id
    private int COLUMN1;
    private String COLUMN2;
    private String COLUMN3;    
    private String COLUMN4;        

    public int getCOLUMN1() {
    	return COLUMN1;
    }
    public void setCOLUMN1(int column1) {
	this.COLUMN1 = column1;
    }
    public String getCOLUMN2() {
	return COLUMN2;
    }
    public void setCOLUMN2(String column2) {
	this.COLUMN2 = column2;
    }
    public String getCOLUMN3() {
	return COLUMN3;
    }
    public void setCOLUMN3(String column3) {
	this.COLUMN3 = column3;
    }
    public String getCOLUMN4() {
	return COLUMN4;
    }
    public void setCOLUMN4(String column4) {
	this.COLUMN4 = column4;
    }    
	@Override
	public DynamicPropertiesManager fetchPropertiesManager() {
		
		// TODO Auto-generated method stub
		return DPM;
		
	}
	
	@Override
	public String toString(){
		String result = COLUMN1 + ":" + COLUMN2 + ":" + COLUMN3 + ":" + COLUMN4 ;
		return result;
	}
	
	
}
