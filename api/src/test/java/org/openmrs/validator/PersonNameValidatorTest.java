/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.validator;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.GlobalProperty;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

/**
 * Tests methods on the {@link PersonNameValidator} class.
 */
public class PersonNameValidatorTest extends BaseContextSensitiveTest {
	
	private PersonNameValidator validator;
	
	private PersonName personName;
	
	private Errors errors;
	
	@Before
	public void setUp() {
		validator = new PersonNameValidator();
		
		personName = new PersonName();
		
		errors = new BindException(personName, "personName");
		
		Context.getAdministrationService().saveGlobalProperty(
		    new GlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_PATIENT_NAME_REGEX, "^[a-zA-Z \\-]+$"));
	}
	
	/**
	 * @see PatientNameValidator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameObjectIsNull() {
		
		validator.validate(null, errors);
		
		Assert.assertTrue(errors.hasErrors());
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameGivenNameIsNull() {
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameGivenNameIsEmpty() {
		
		personName.setGivenName("");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameGivenNameIsJustSpaces() {
		
		personName.setGivenName("    ");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameGivenNameIsSpacesSurroundedByQuotationMarks() {
		
		personName.setGivenName("\"   \"");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameGivenNameIsNotBlank() {
		
		personName.setGivenName("Fred");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("givenName"));
		
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNameIsNull() {
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNameIsEmpty() {
		
		personName.setFamilyName("");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNameIsJustSpaces() {
		
		personName.setFamilyName("    ");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameFamilyNameIsSpacesSurroundedByQuotationMarks() {
		
		personName.setFamilyName("\"   \"");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNameIsNotBlank() {
		
		personName.setFamilyName("Rogers");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNamePrefixIsTooLong() {
		
		personName.setGivenName("givenName");
		personName.setFamilyName("familyName");
		personName
		        .setPrefix("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"); // 100 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("prefix"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNamePrefixIsExactlyMaxLength() {
		
		personName.setPrefix("12345678901234567890123456789012345678901234567890"); // exactly 50 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("prefix"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNamePrefixIsLessThanMaxFieldLength() {
		
		personName.setPrefix("1234567890");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("prefix"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameGivenNameIsTooLong() {
		
		personName
		        .setGivenName("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"); // 100 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameGivenNameIsExactlyMaxLength() {
		
		personName.setGivenName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij"); // exactly 50 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameGivenNameIsLessThanMaxFieldLength() {
		
		personName.setGivenName("abcdefghij");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameMiddleNameIsTooLong() {
		
		personName
		        .setMiddleName("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"); // 100 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("middleName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameMiddleNameIsExactlyMaxLength() {
		
		personName.setMiddleName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij"); // exactly 50 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("middleName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameMiddleNameIsLessThanMaxFieldLength() {
		
		personName.setMiddleName("abcdefghij");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("middleName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameFamilyNamePrefixIsTooLong() {
		
		personName.setGivenName("givenName");
		personName.setFamilyName("familyName");
		personName
		        .setFamilyNamePrefix("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"); // 100 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyNamePrefix"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNamePrefixIsExactlyMaxLength() {
		
		personName.setFamilyNamePrefix("12345678901234567890123456789012345678901234567890"); // exactly 50 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyNamePrefix"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNamePrefixIsLessThanMaxFieldLength() {
		
		personName.setFamilyNamePrefix("1234567890");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyNamePrefix"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameFamilyNameIsTooLong() {
		
		personName
		        .setFamilyName("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"); // 100 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNameIsExactlyMaxLength() {
		
		personName.setFamilyName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij"); // exactly 50 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNameIsLessThanMaxFieldLength() {
		
		personName.setFamilyName("abcdefghij");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameFamilyName2IsTooLong() {
		
		personName
		        .setFamilyName2("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"); // 100 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyName2"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyName2IsExactlyMaxLength() {
		
		personName.setFamilyName2("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij"); // exactly 50 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName2"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyName2IsLessThanMaxFieldLength() {
		
		personName.setFamilyName2("abcdefghij");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName2"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameFamilyNameSuffixIsTooLong() {
		
		personName.setGivenName("givenName");
		personName.setFamilyName("familyName");
		personName
		        .setFamilyNameSuffix("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"); // 100 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyNameSuffix"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNameSuffixIsExactlyMaxLength() {
		
		personName.setFamilyNameSuffix("12345678901234567890123456789012345678901234567890"); // exactly 50 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyNameSuffix"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNameSuffixIsLessThanMaxFieldLength() {
		
		personName.setFamilyNameSuffix("1234567890");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyNameSuffix"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameDegreeIsTooLong() {
		
		personName.setGivenName("givenName");
		personName.setFamilyName("familyName");
		personName
		        .setDegree("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"); // 100 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("degree"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameDegreeIsExactlyMaxLength() {
		
		personName.setDegree("12345678901234567890123456789012345678901234567890"); // exactly 50 characters long
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("degree"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameDegreeIsLessThanMaxFieldLength() {
		
		personName.setDegree("1234567890");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("degree"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameGivenNameIsInvalid() {
		
		personName.setGivenName("34dfgd");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameGivenNameIsValid() {
		
		personName.setGivenName("alex");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameMiddleNameIsInvalid() {
		
		personName.setMiddleName("34dfgd");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("middleName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameMiddleNameIsValid() {
		
		personName.setMiddleName("de");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("middleName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameFamilyNameIsInvalid() {
		
		personName.setFamilyName("34dfgd");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyNameIsValid() {
		
		personName.setFamilyName("souza");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldFailValidationIfPersonNameFamilyName2IsInvalid() {
		
		personName.setFamilyName2("34dfgd");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyName2"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldPassValidationIfPersonNameFamilyName2IsValid() {
		
		personName.setFamilyName2("souza-");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName2"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldSkipRegexValidationIfValidationStringIsNull() {
		
		Context.getAdministrationService().saveGlobalProperty(
		    new GlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_PATIENT_NAME_REGEX, null));
		personName.setFamilyName("asd123");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	public void validate_shouldSkipRegexValidationIfValidationStringIsEmpty() {
		
		Context.getAdministrationService().saveGlobalProperty(
		    new GlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_PATIENT_NAME_REGEX, ""));
		personName.setGivenName("123asd");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PersonNameValidator#validatePersonName(PersonName,Errors,null,null)
	 */
	@Test
	public void validatePersonName_shouldNotValidateAgainstRegexForBlankNames() {
		
		personName.setGivenName("given");
		personName.setFamilyName("family");
		personName.setMiddleName("");
		personName.setFamilyName2("");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertFalse(errors.hasErrors());
	}
	
	/**
	 * @see PersonNameValidator#validate(Object,Errors)
	 */
	@Test
	public void validate_shouldPassValidationIfNameIsInvalidButVoided() {
		
		personName.setVoided(true);
		personName.setFamilyName2("34dfgd"); // invalid
		
		validator.validate(personName, errors);
		
		Assert.assertFalse(errors.hasErrors());
	}
	
	/**
	 * @see PersonNameValidator#validate(Object,Errors)
	 */
	@Test
	public void validate_shouldPassValidationIfFieldLengthsAreCorrect() {
		
		personName.setPrefix("prefix");
		personName.setGivenName("givenName");
		personName.setMiddleName("middleName");
		personName.setFamilyNamePrefix("familyNamePrefix");
		personName.setFamilyName("familyName");
		personName.setFamilyName2("familyName");
		personName.setFamilyNameSuffix("familyNameSuffix");
		personName.setDegree("degree");
		personName.setVoidReason("voidReason");
		
		validator.validate(personName, errors);
		
		Assert.assertFalse(errors.hasErrors());
	}
	
	/**
	 * @see PersonNameValidator#validate(Object,Errors)
	 */
	@Test
	public void validate_shouldFailValidationIfFieldLengthsAreNotCorrect() {
		
		personName.setPrefix("too long text too long text too long text too long text");
		personName.setGivenName("too long text too long text too long text too long text");
		personName.setMiddleName("too long text too long text too long text too long text");
		personName.setFamilyName("too long text too long text too long text too long text");
		personName.setFamilyNamePrefix("too long text too long text too long text too long text");
		personName.setFamilyName("too long text too long text too long text too long text");
		personName.setFamilyName2("too long text too long text too long text too long text");
		personName.setFamilyNameSuffix("too long text too long text too long text too long text");
		personName.setDegree("too long text too long text too long text too long text");
		personName
		        .setVoidReason("too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text");
		
		validator.validate(personName, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("prefix"));
		Assert.assertTrue(errors.hasFieldErrors("givenName"));
		Assert.assertTrue(errors.hasFieldErrors("familyNamePrefix"));
		Assert.assertTrue(errors.hasFieldErrors("familyName"));
		Assert.assertTrue(errors.hasFieldErrors("familyName2"));
		Assert.assertTrue(errors.hasFieldErrors("familyNameSuffix"));
		Assert.assertTrue(errors.hasFieldErrors("degree"));
		Assert.assertTrue(errors.hasFieldErrors("middleName"));
		Assert.assertTrue(errors.hasFieldErrors("voidReason"));
	}

    /**
     * @see PersonNameValidator#validatePersonName(PersonName, Errors, Boolean, Boolean)
     */
    @Test
	public void validatePersonName_shouldReportErrorsWithNonStandardPrefixWhenCalledInHistoricWay() {
		
		PersonName personName = new PersonName("", "reb", "feb");
		MapBindingResult errors = new MapBindingResult(new HashMap<String, Object>(), "personName");
		
		validator.validatePersonName(personName, errors, true, false);
		
		Assert.assertTrue(errors.hasFieldErrors("names[0]." + "givenName"));
    }

	/**
	 * @see PersonNameValidator#validate(Object, Errors)
	 */
	@Test
	public void validate_shouldReportErrorsOnCorrectFieldNames() {
		
		PersonName personName = new PersonName("", "reb", "feb");
		MapBindingResult errors = new MapBindingResult(new HashMap<String, Object>(), "personName");
		
		validator.validate(personName, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	@Ignore("Unignore after investigating and fixing - RA-543")
	public void validate_shouldFailValidationIfPersonNameGivenNameHasLeadingSpaces() {
		
		personName.setGivenName(" alex");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("givenName"));
	}

	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	@Ignore("Unignore after investigating and fixing - RA-543")
	public void validate_shouldFailValidationIfPersonNameGivenNameHasTrailingSpaces() {
		
		personName.setGivenName("alex ");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("givenName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	@Ignore("Unignore after investigating and fixing - RA-543")
	public void validate_shouldFailValidationIfPersonNameMiddleNameHasLeadingSpaces() {
		
		personName.setMiddleName(" de");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("middleName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	@Ignore("Unignore after investigating and fixing - RA-543")
	public void validate_shouldFailValidationIfPersonNameMiddleNameHasTrailingSpaces() {
		
		personName.setMiddleName("de ");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("middleName"));
	}

	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	@Ignore("Unignore after investigating and fixing - RA-543")
	public void validate_shouldFailValidationIfPersonNameFamilyNameHasLeadingSpaces() {
		
		personName.setFamilyName(" souza");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyName"));
	}

	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	@Ignore("Unignore after investigating and fixing - RA-543")
	public void validate_shouldFailValidationIfPersonNameFamilyNameHasTrailingSpaces() {
		
		personName.setFamilyName("souza ");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyName"));
	}
	
	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	@Ignore("Unignore after investigating and fixing - RA-543")
	public void validate_shouldFailValidationIfPersonNameFamilyName2HasLeadingSpaces() {
		
		personName.setFamilyName2(" souza-");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyName2"));
	}

	/**
	 * @see PatientNameValidator#validatePersonName(java.lang.Object, org.springframework.validation.Errors, boolean, boolean)
	 */
	@Test
	@Ignore("Unignore after investigating and fixing - RA-543")
	public void validate_shouldFailValidationIfPersonNameFamilyName2HasTrailingSpaces() {
		
		personName.setFamilyName2("souza- ");
		
		validator.validatePersonName(personName, errors, false, true);
		
		Assert.assertTrue(errors.hasFieldErrors("familyName2"));
	}
}
