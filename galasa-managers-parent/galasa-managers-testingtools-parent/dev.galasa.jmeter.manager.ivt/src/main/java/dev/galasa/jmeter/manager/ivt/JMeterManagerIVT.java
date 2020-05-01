package dev.galasa.jmeter.manager.ivt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.logging.Log;

import dev.galasa.Test;
import dev.galasa.artifact.BundleResources;
import dev.galasa.artifact.IBundleResources;
import dev.galasa.artifact.TestBundleResourceException;
import dev.galasa.core.manager.Logger;
import dev.galasa.jmeter.IJMeterSession;
import dev.galasa.jmeter.JMeterManagerException;
import dev.galasa.jmeter.JMeterSession;

/*
 * Licensed Materials - Property of IBM
 * 
 * (c) Copyright IBM Corp. 2020.
 */

 @Test
 public class JMeterManagerIVT {

    @Logger
    public Log logger;

    @BundleResources
    public IBundleResources resources;

    @JMeterSession(jmxPath = "DynamicTest.jmx", propPath = "jmeter.properties")
    public IJMeterSession session;

    @JMeterSession(jmxPath = "ExistingTest.jmx")
    public IJMeterSession session2;

    @Test
    public void provisionedNotNull() throws Exception {

      assertThat(logger).isNotNull();
      assertThat(session).isNotNull();
      assertThat(session2).isNotNull();
    
    }

    @Test
    public void startJMeterTestWithDynamicHosts() throws JMeterManagerException, TestBundleResourceException {
      InputStream jmxStream = resources.retrieveFile("/DynamicTest.jmx");
      InputStream propStream = resources.retrieveFile("/jmeter.properties");

      /**
       * Substituting variables from the adapted JMX-file 
       * If we want to dynamically change the host, the JMX-file needs to have $VARIABLE to substitute
       * This is a special case to adapt JMX-files to make them dynamic during runtime
       *  */ 
      HashMap<String,Object> map = new HashMap<String,Object>();
      map.put("HOST", "galasa.dev");
      map.put("PATH", "/docs");

      session.setChangedParametersJmxFile(jmxStream, map);
      session.applyProperties(propStream);
      session.startJmeter();
      assertThat(session.statusTest()).isTrue();
    }

    @Test
    public void startJMeterTestStatically() throws JMeterManagerException, TestBundleResourceException {
      InputStream jmxStream = resources.retrieveFile("/ExistingTest.jmx");

      session2.setDefaultGeneratedJmxFile(jmxStream);
      session2.startJmeter();

      assertThat(session2.statusTest()).isTrue();
    }

  
 }