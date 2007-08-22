/* 
 * Copyright 2004-2006 OpenSymphony 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 */
package org.quartz;

import java.util.Calendar;
import java.util.TimeZone;

import org.quartz.impl.calendar.AnnualCalendar;


/**
 * Unit test for AnnualCalendar serialization backwards compatibility.
 */
public class AnnualCalendarTest extends SerializationTestSupport {
    private static final String[] VERSIONS = new String[] {"1.5.1"};
    
    private static final TimeZone EST_TIME_ZONE = TimeZone.getTimeZone("America/New_York"); 

    /**
     * Get the object to serialize when generating serialized file for future
     * tests, and against which to validate deserialized object.
     */
    protected Object getTargetObject() {
        AnnualCalendar c = new AnnualCalendar();
        
        c.setDescription("description");
        
        Calendar cal = Calendar.getInstance(EST_TIME_ZONE);
        cal.clear();
        cal.set(2005, Calendar.JANUARY, 20, 10, 5, 15);
        
        c.setDayExcluded(cal, true);
        
        return c;
    }
    
    /**
     * Get the Quartz versions for which we should verify
     * serialization backwards compatibility.
     */
    protected String[] getVersions() {
        return VERSIONS;
    }
    
    /**
     * Verify that the target object and the object we just deserialized 
     * match.
     */
    protected void verifyMatch(Object target, Object deserialized) {
        AnnualCalendar targetCalendar = (AnnualCalendar)target;
        AnnualCalendar deserializedCalendar = (AnnualCalendar)deserialized;
        
        assertNotNull(deserializedCalendar);
        assertEquals(targetCalendar.getDescription(), deserializedCalendar.getDescription());
        assertEquals(targetCalendar.getDaysExcluded(), deserializedCalendar.getDaysExcluded());
        assertNull(deserializedCalendar.getTimeZone());
    }

    /**
     * Tests if method <code>setDaysExcluded</code> protects the property daysExcluded against nulling.
     * See: QUARTZ-590
     */
    public void testDaysExcluded() {
		AnnualCalendar annualCalendar = new AnnualCalendar();
		
		annualCalendar.setDaysExcluded(null);
		
		assertNotNull("Annual calendar daysExcluded property should have been set to empty ArrayList, not null.",annualCalendar.getDaysExcluded());
    }

    /**
     * Tests the parameter <code>exclude</code> in a method <code>setDaysExcluded</code>
     * of class <code>org.quartz.impl.calendar.AnnualCalendar</code>
     */
    public void testExclude() {
        AnnualCalendar annualCalendar = new AnnualCalendar();
        Calendar day = Calendar.getInstance();

        day.set(Calendar.MONTH, 10);
        day.set(Calendar.DAY_OF_MONTH, 15);
        annualCalendar.setDayExcluded(day, false);

        assertTrue("The day 15 October is not expected to be excluded but it is", !annualCalendar.isDayExcluded(day));

        day.set(Calendar.MONTH, 10);
        day.set(Calendar.DAY_OF_MONTH, 15);
        annualCalendar.setDayExcluded(day, true);

        day.set(Calendar.MONTH, 11);
        day.set(Calendar.DAY_OF_MONTH, 12);
        annualCalendar.setDayExcluded(day, true);

        day.set(Calendar.MONTH, 9);
        day.set(Calendar.DAY_OF_MONTH, 1);
        annualCalendar.setDayExcluded(day, true);

        assertTrue("The day 15 October is expected to be excluded but it is not", annualCalendar.isDayExcluded(day));

        day.set(Calendar.MONTH, 10);
        day.set(Calendar.DAY_OF_MONTH, 15);
        annualCalendar.setDayExcluded(day, false);

        assertTrue("The day 15 October is not expected to be excluded but it is", !annualCalendar.isDayExcluded(day));
    }

}
