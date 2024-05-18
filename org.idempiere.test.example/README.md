Examples for using process, process parameter annotations and Core.getMappedProcessFactory().

1. To Test:
* Import project to your iDempiere Eclipse workspace.
* Duplicate idempiere.unit.test launch configuration.
* At the duplicated idempiere.unit.test launch configuration, goes to the plugin tab and select this plugin.
* Launch the duplicated idempiere.unit.test launch configuration, at the Test tab, select Run a single test, select this project and select MyTestCase as the test class.

2. POM Files
* pom.xml
	* Use repository in the `<repositories>` section of POM for resolution of iDempiere core and target platform bundles.
	* Parent pom is ../parent-repository-pom.xml.
* pom-targetplaform.xml
	* Use custom target platform definition (idempiere.core.repository.target) for resolution of iDempiere core and target platform bundles.
	* Parent pom is ../parent-targetplatform-pom.xml.
* pom-core-parent.xml
	* Use locally install iDempiere bundles for resolution of iDempiere core bundles.
	* Use iDempiere core target platform definition for resolution of target platform bundles.
	* Parent pom is idempiere/org.idempiere.parent/pom.xml.
	* Pre-requisite: run `mvn install` at idempiere source folder to install iDempiere core bundles into local repository.

3. Folder structure for pom.xml and pom-targetplaform.xml  
```
workspace folder  
|  
+--- idempiere  
|  
|    +--- org.idempiere.parent  
|  
+--- idempiere-examples  
|  
|    +--- org.idempiere.test.example  
```

4. Run test using maven
* mvn verify -DskipTests=false
* mvn --file pom-targetplatform.xml verify -DskipTests=false
* mvn --file pom-core-parent.xml verify -DskipTests=false