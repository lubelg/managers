/*
 * Licensed Materials - Property of IBM
 * 
 * (c) Copyright IBM Corp. 2020.
 */
package dev.galasa.zos.internal.properties;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import dev.galasa.framework.spi.ConfigurationPropertyStoreException;
import dev.galasa.framework.spi.IConfigurationPropertyStoreService;
import dev.galasa.framework.spi.cps.CpsProperties;
import dev.galasa.zos.ZosManagerException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ZosPropertiesSingleton.class, CpsProperties.class})
public class TestImageMaxSlots {
    
    @Mock
    private IConfigurationPropertyStoreService configurationPropertyStoreServiceMock;
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    
    private static final String IMAGE_ID = "image";
    
    private static final int MAX_SLOTS = 99;
    
    private static final int DEFAULT_MAX_SLOTS = 2;
    
    @Test
    public void testConstructor() {
        ImageMaxSlots imageMaxSlots = new ImageMaxSlots();
        Assert.assertNotNull("Object was not created", imageMaxSlots);
    }
    
    @Test
    public void testNull() throws Exception {
        Assert.assertEquals("Unexpected value returned from ImageMaxSlots.get()", DEFAULT_MAX_SLOTS, getProperty(IMAGE_ID, null));
    }
    
    @Test
    public void testValid() throws Exception {
        Assert.assertEquals("Unexpected value returned from ImageMaxSlots.get()", MAX_SLOTS, getProperty(IMAGE_ID, String.valueOf(MAX_SLOTS)));
    }
    
    @Test
    public void testException() throws Exception {
        exceptionRule.expect(ZosManagerException.class);
        exceptionRule.expectMessage("Problem asking the CPS for the zOS image "  + null + " max slots");
        
        getProperty(null, null, true);
    }

    private int getProperty(String arg, String value) throws Exception {
        return getProperty(arg, value, false);
    }
    
    private int getProperty(String arg, String value, boolean exception) throws Exception {
        PowerMockito.spy(ZosPropertiesSingleton.class);
        PowerMockito.doReturn(configurationPropertyStoreServiceMock).when(ZosPropertiesSingleton.class, "cps");
        PowerMockito.spy(CpsProperties.class);
        
        if (!exception) {
            PowerMockito.doReturn(value).when(CpsProperties.class, "getStringNulled", Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.any());
        } else {
            PowerMockito.doThrow(new ConfigurationPropertyStoreException()).when(CpsProperties.class, "getStringNulled", Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.any());
        }
        
        return ImageMaxSlots.get(arg);
    }
}
