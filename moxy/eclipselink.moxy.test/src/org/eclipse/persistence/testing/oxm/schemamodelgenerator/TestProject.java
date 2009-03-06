/*******************************************************************************
* Copyright (c) 1998, 2008 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
* which accompanies this distribution.
* The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
* and the Eclipse Distribution License is available at
* http://www.eclipse.org/org/documents/edl-v10.php.
*
* Contributors:
* dmccann - Mar 2/2009 - 2.0 - Initial implementation
******************************************************************************/
package org.eclipse.persistence.testing.oxm.schemamodelgenerator;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.eclipse.persistence.internal.queries.ContainerPolicy;
import org.eclipse.persistence.mappings.converters.TypeConversionConverter;
import org.eclipse.persistence.oxm.NamespaceResolver;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.oxm.mappings.XMLCompositeCollectionMapping;
import org.eclipse.persistence.oxm.mappings.XMLCompositeDirectCollectionMapping;
import org.eclipse.persistence.oxm.mappings.XMLCompositeObjectMapping;
import org.eclipse.persistence.oxm.mappings.XMLDirectMapping;
import org.eclipse.persistence.oxm.schema.XMLSchemaReference;
import org.eclipse.persistence.oxm.schema.XMLSchemaURLReference;
import org.eclipse.persistence.sessions.Project;

public class TestProject extends Project {
    NamespaceResolver nsr;
    boolean setSchemaContext, setDefaultRootElement;
    
    public TestProject() {
        nsr = new NamespaceResolver();
        nsr.setDefaultNamespaceURI("myns:examplenamespace");
        setSchemaContext = true;
        setDefaultRootElement = true;
        addDescriptors();
    }
    
    public TestProject(boolean setSchemaContext, boolean setDefaultRootElement) {
        nsr = new NamespaceResolver();
        nsr.setDefaultNamespaceURI("myns:examplenamespace");
        this.setSchemaContext = setSchemaContext;
        this.setDefaultRootElement = setDefaultRootElement;
        addDescriptors();
    }

    public TestProject(NamespaceResolver resolver, boolean setSchemaContext, boolean setDefaultRootElement) {
        nsr = resolver;
        this.setSchemaContext = setSchemaContext;
        this.setDefaultRootElement = setDefaultRootElement;
        addDescriptors();
    }
    
    private void addDescriptors() {
        addDescriptor(getEmployeeDescriptor());
        addDescriptor(getAddressDescriptor());
        addDescriptor(getPhoneNumberDescriptor());
    }
    
    private XMLDescriptor getEmployeeDescriptor() {
        XMLDescriptor descriptor = new XMLDescriptor();
        descriptor.setJavaClass(Employee.class);
        descriptor.setAlias("Employee");
        descriptor.setNamespaceResolver(nsr);
        if (setSchemaContext) {
            XMLSchemaReference sRef = new XMLSchemaURLReference();
            sRef.setSchemaContext("/employee-type");
            sRef.setType(XMLSchemaReference.COMPLEX_TYPE);
            descriptor.setSchemaReference(sRef);
        }
        if (setDefaultRootElement) {
            descriptor.setDefaultRootElement("employee");
        }
        // create name mapping
        XMLDirectMapping nameMapping = new XMLDirectMapping();
        nameMapping.setAttributeName("name");
        nameMapping.setXPath("name/text()");
        descriptor.addMapping(nameMapping);
        // create address mapping
        XMLCompositeObjectMapping addressMapping = new XMLCompositeObjectMapping();
        addressMapping.setAttributeName("address");
        addressMapping.setXPath("address");
        addressMapping.setReferenceClass(Address.class);
        descriptor.addMapping(addressMapping);
        // create phone numbers mapping
        XMLCompositeCollectionMapping phonesMapping = new XMLCompositeCollectionMapping();
        phonesMapping.setAttributeName("phoneNumbers");
        phonesMapping.useCollectionClass(ArrayList.class);
        phonesMapping.setXPath("phone-numbers");
        phonesMapping.setReferenceClass(PhoneNumber.class);
        descriptor.addMapping(phonesMapping);
        // create project ids mapping
        XMLCompositeDirectCollectionMapping projectIdsMapping = new XMLCompositeDirectCollectionMapping();
        projectIdsMapping.setAttributeName("projectIDs");
        projectIdsMapping.useCollectionClass(ArrayList.class);
        projectIdsMapping.setXPath("project-id");
        TypeConversionConverter tcc = new TypeConversionConverter(projectIdsMapping);
        tcc.setObjectClass(BigDecimal.class);
        projectIdsMapping.setValueConverter(tcc);
        descriptor.addMapping(projectIdsMapping);
        return descriptor;
    }

    private XMLDescriptor getAddressDescriptor() {
        XMLDescriptor descriptor = new XMLDescriptor();
        descriptor.setJavaClass(Address.class);
        descriptor.setAlias("Address");
        descriptor.setNamespaceResolver(nsr);
        if (setSchemaContext) {
            XMLSchemaReference sRef = new XMLSchemaURLReference();
            sRef.setSchemaContext("/address-type");
            sRef.setType(XMLSchemaReference.COMPLEX_TYPE);
            descriptor.setSchemaReference(sRef);
        }
        if (setDefaultRootElement) {
            descriptor.setDefaultRootElement("address");
        }
        // create street mapping
        XMLDirectMapping streetMapping = new XMLDirectMapping();
        streetMapping.setAttributeName("street");
        streetMapping.setXPath("street/text()");
        descriptor.addMapping(streetMapping);
        // create city mapping
        XMLDirectMapping cityMapping = new XMLDirectMapping();
        cityMapping.setAttributeName("city");
        cityMapping.setXPath("city/text()");
        descriptor.addMapping(cityMapping);
        // create country mapping
        XMLDirectMapping countryMapping = new XMLDirectMapping();
        countryMapping.setAttributeName("country");
        countryMapping.setXPath("country/text()");
        descriptor.addMapping(countryMapping);
        // create postal code mapping
        XMLDirectMapping postalMapping = new XMLDirectMapping();
        postalMapping.setAttributeName("postalCode");
        postalMapping.setXPath("@postal-code");
        descriptor.addMapping(postalMapping);
        return descriptor;
    }

    private XMLDescriptor getPhoneNumberDescriptor() {
        XMLDescriptor descriptor = new XMLDescriptor();
        descriptor.setJavaClass(PhoneNumber.class);
        descriptor.setAlias("PhoneNumber");
        descriptor.setNamespaceResolver(nsr);
        if (setSchemaContext) {
            XMLSchemaReference sRef = new XMLSchemaURLReference();
            sRef.setSchemaContext("/phone-number-type");
            sRef.setType(XMLSchemaReference.COMPLEX_TYPE);
            descriptor.setSchemaReference(sRef);
        }
        if (setDefaultRootElement) {
            descriptor.setDefaultRootElement("phone-number");
        }
        // create number mapping
        XMLDirectMapping numberMapping = new XMLDirectMapping();
        numberMapping.setAttributeName("number");
        numberMapping.setXPath("number/text()");
        descriptor.addMapping(numberMapping);
        return descriptor;
    }
}
