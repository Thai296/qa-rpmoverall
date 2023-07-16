package com.overall.utils;

import java.util.HashMap;
import java.util.Map;

public class EligibilityConstants
{
    public static enum ServiceTypeCode
    {
        SERVICETYPECODE_NULL("", " "),
        SERVICETYPECODE_NONE("0", "Not Provided"),
        SERVICETYPECODE_1("1", "1 - Medical Care"),
        SERVICETYPECODE_2("2", "2 - Surgical"),
        SERVICETYPECODE_3("3", "3 - Consultation"),
        SERVICETYPECODE_4("4", "4 - Diagnostic X-Ray"),
        SERVICETYPECODE_5("5", "5 - Diagnostic Lab"),
        SERVICETYPECODE_6("6", "6 - Radiation Therapy"),
        SERVICETYPECODE_7("7", "7 - Anesthesia"),
        SERVICETYPECODE_8("8", "8 - Surgical Assistance"),
        SERVICETYPECODE_9("9", "9 - Other Medical"),
        SERVICETYPECODE_10("10", "10 - Blood Charges"),
        SERVICETYPECODE_11("11", "11 - Used Durable Medical Equipment"),
        SERVICETYPECODE_12("12", "12 - Durable Medical Equipment Purchase"),
        SERVICETYPECODE_13("13", "13 - Ambulatory Service Center Facility"),
        SERVICETYPECODE_14("14", "14 - Renal Supplies in the Home"),
        SERVICETYPECODE_15("15", "15 - Alternate Method Dialysis"),
        SERVICETYPECODE_16("16", "16 - Chronic Renal Disease (CRD) Equipment"),
        SERVICETYPECODE_17("17", "17 - Pre-Admission Testing"),
        SERVICETYPECODE_18("18", "18 - Durable Medical Equipment Rental"),
        SERVICETYPECODE_19("19", "19 - Pneumonia Vaccine"),
        SERVICETYPECODE_20("20", "20 - Second Surgical Opinion"),
        SERVICETYPECODE_21("21", "21 - Third Surgical Opinion"),
        SERVICETYPECODE_22("22", "22 - Social Work"),
        SERVICETYPECODE_23("23", "23 - Diagnostic Dental"),
        SERVICETYPECODE_24("24", "24 - Periodontics"),
        SERVICETYPECODE_25("25", "25 - Restorative"),
        SERVICETYPECODE_26("26", "26 - Endodontics"),
        SERVICETYPECODE_27("27", "27 - Maxillofacial Prosthetics"),
        SERVICETYPECODE_28("28", "28 - Adjunctive Dental Services"),
        SERVICETYPECODE_30("30", "30 - Health Benefit Plan Coverage"),
        SERVICETYPECODE_32("32", "32 - Plan Waiting Period"),
        SERVICETYPECODE_33("33", "33 - Chiropractic"),
        SERVICETYPECODE_34("34", "34 - Chiropractic Office Visits"),
        SERVICETYPECODE_35("35", "35 - Dental Care"),
        SERVICETYPECODE_36("36", "36 - Dental Crowns"),
        SERVICETYPECODE_37("37", "37 - Dental Accident"),
        SERVICETYPECODE_38("38", "38 - Orthodontics"),
        SERVICETYPECODE_39("39", "39 - Prosthodontics"),
        SERVICETYPECODE_40("40", "40 - Oral Surgery"),
        SERVICETYPECODE_41("41", "41 - Routine (Preventive) Dental"),
        SERVICETYPECODE_42("42", "42 - Home Health Care"),
        SERVICETYPECODE_43("43", "43 - Home Health Prescriptions"),
        SERVICETYPECODE_44("44", "44 - Home Health Visits"),
        SERVICETYPECODE_45("45", "45 - Hospice"),
        SERVICETYPECODE_46("46", "46 - Respite Care"),
        SERVICETYPECODE_47("47", "47 - Hospital"),
        SERVICETYPECODE_48("48", "48 - Hospital - Inpatient"),
        SERVICETYPECODE_49("49", "49 - Hospital - Room and Board"),
        SERVICETYPECODE_50("50", "50 - Hospital - Outpatient"),
        SERVICETYPECODE_51("51", "51 - Hospital - Emergency Accident"),
        SERVICETYPECODE_52("52", "52 - Hospital - Emergency Medical"),
        SERVICETYPECODE_53("53", "53 - Hospital - Ambulatory Surgical"),
        SERVICETYPECODE_54("54", "54 - Long Term Care"),
        SERVICETYPECODE_55("55", "55 - Major Medical"),
        SERVICETYPECODE_56("56", "56 - Medically Related Transportation"),
        SERVICETYPECODE_57("57", "57 - Air Transportation"),
        SERVICETYPECODE_58("58", "58 - Cabulance"),
        SERVICETYPECODE_59("59", "59 - Licensed Ambulance"),
        SERVICETYPECODE_60("60", "60 - General Benefits"),
        SERVICETYPECODE_61("61", "61 - In-vitro Fertilization"),
        SERVICETYPECODE_62("62", "62 - MRI/CAT Scan"),
        SERVICETYPECODE_63("63", "63 - Donor Procedures"),
        SERVICETYPECODE_64("64", "64 - Acupuncture"),
        SERVICETYPECODE_65("65", "65 - Newborn Care"),
        SERVICETYPECODE_66("66", "66 - Pathology"),
        SERVICETYPECODE_67("67", "67 - Smoking Cessation"),
        SERVICETYPECODE_68("68", "68 - Well Baby Care"),
        SERVICETYPECODE_69("69", "69 - Maternity"),
        SERVICETYPECODE_70("70", "70 - Transplants"),
        SERVICETYPECODE_71("71", "71 - Audiology Exam"),
        SERVICETYPECODE_72("72", "72 - Inhalation Therapy"),
        SERVICETYPECODE_73("73", "73 - Diagnostic Medical"),
        SERVICETYPECODE_74("74", "74 - Private Duty Nursing"),
        SERVICETYPECODE_75("75", "75 - Prosthetic Device"),
        SERVICETYPECODE_76("76", "76 - Dialysis"),
        SERVICETYPECODE_77("77", "77 - Otological Exam"),
        SERVICETYPECODE_78("78", "78 - Chemotherapy"),
        SERVICETYPECODE_79("79", "79 - Allergy Testing"),
        SERVICETYPECODE_80("80", "80 - Immunizations"),
        SERVICETYPECODE_81("81", "81 - Routine Physical"),
        SERVICETYPECODE_82("82", "82 - Family Planning"),
        SERVICETYPECODE_83("83", "83 - Infertility"),
        SERVICETYPECODE_84("84", "84 - Abortion"),
        SERVICETYPECODE_85("85", "85 - AIDS"),
        SERVICETYPECODE_86("86", "86 - Emergency Services"),
        SERVICETYPECODE_87("87", "87 - Cancer"),
        SERVICETYPECODE_88("88", "88 - Pharmacy"),
        SERVICETYPECODE_89("89", "89 - Free Standing Prescription Drug"),
        SERVICETYPECODE_90("90", "90 - Mail Order Prescription Drug"),
        SERVICETYPECODE_91("91", "91 - Brand Name Prescription Drug"),
        SERVICETYPECODE_92("92", "92 - Generic Prescription Drug"),
        SERVICETYPECODE_93("93", "93 - Podiatry"),
        SERVICETYPECODE_94("94", "94 - Podiatry - Office Visits"),
        SERVICETYPECODE_95("95", "95 - Podiatry - Nursing Home Visits"),
        SERVICETYPECODE_96("96", "96 - Professional (Physician)"),
        SERVICETYPECODE_97("97", "97 - Anesthesiologist"),
        SERVICETYPECODE_98("98", "98 - Professional (Physician) Visit - Office"),
        SERVICETYPECODE_99("99", "99 - Professional (Physician) Visit - Inpatient"),
        SERVICETYPECODE_A0("A0", "A0 - Professional (Physician) Visit - Outpatient"),
        SERVICETYPECODE_A1("A1", "A1 - Professional (Physician) Visit - Nursing Home"),
        SERVICETYPECODE_A2("A2", "A2 - Professional (Physician) Visit - Skilled Nursing Facility"),
        SERVICETYPECODE_A3("A3", "A3 - Professional (Physician) Visit - Home"),
        SERVICETYPECODE_A4("A4", "A4 - Psychiatric"),
        SERVICETYPECODE_A5("A5", "A5 - Psychiatric - Room and Board"),
        SERVICETYPECODE_A6("A6", "A6 - Psychotherapy"),
        SERVICETYPECODE_A7("A7", "A7 - Psychiatric - Inpatient"),
        SERVICETYPECODE_A8("A8", "A8 - Psychiatric - Outpatient"),
        SERVICETYPECODE_A9("A9", "A9 - Rehabilitation"),
        SERVICETYPECODE_AA("AA", "AA - Rehabilitation - Room and Board"),
        SERVICETYPECODE_AB("AB", "AB - Rehabilitation - Inpatient"),
        SERVICETYPECODE_AC("AC", "AC - Rehabilitation - Outpatient"),
        SERVICETYPECODE_AD("AD", "AD - Occupational Therapy"),
        SERVICETYPECODE_AE("AE", "AE - Physical Medicine"),
        SERVICETYPECODE_AF("AF", "AF - Speech Therapy"),
        SERVICETYPECODE_AG("AG", "AG - Skilled Nursing Care"),
        SERVICETYPECODE_AH("AH", "AH - Skilled Nursing Care - Room and Board"),
        SERVICETYPECODE_AI("AI", "AI - Substance Abuse"),
        SERVICETYPECODE_AJ("AJ", "AJ - Alcoholism"),
        SERVICETYPECODE_AK("AK", "AK - Drug Addiction"),
        SERVICETYPECODE_AL("AL", "AL - Vision (Optometry)"),
        SERVICETYPECODE_AM("AM", "AM - Frames"),
        SERVICETYPECODE_AN("AN", "AN - Routine Exam"),
        SERVICETYPECODE_AO("AO", "AO - Lenses"),
        SERVICETYPECODE_AQ("AQ", "AQ - Nonmedically Necessary Physical"),
        SERVICETYPECODE_AR("AR", "AR - Experimental Drug Therapy"),
        SERVICETYPECODE_B1("B1", "B1 - Burn Care"),
        SERVICETYPECODE_B2("B2", "B2 - Brand Name Prescription Drug - Formulary"),
        SERVICETYPECODE_B3("B3", "B3 - Brand Name Prescription Drug - Non-Formulary"),
        SERVICETYPECODE_BA("BA", "BA - Independent Medical Evaluation"),
        SERVICETYPECODE_BB("BB", "BB - Partial Hospitalization (Psychiatric)"),
        SERVICETYPECODE_BC("BC", "BC - Day Care (Psychiatric)"),
        SERVICETYPECODE_BD("BD", "BD - Cognitive Therapy"),
        SERVICETYPECODE_BE("BE", "BE - Massage Therapy"),
        SERVICETYPECODE_BF("BF", "BF - Pulmonary Rehabilitation"),
        SERVICETYPECODE_BG("BG", "BG - Cardiac Rehabilitation"),
        SERVICETYPECODE_BH("BH", "BH - Pediatric"),
        SERVICETYPECODE_BI("BI", "BI - Nursery"),
        SERVICETYPECODE_BJ("BJ", "BJ - Skin"),
        SERVICETYPECODE_BK("BK", "BK - Orthopedic"),
        SERVICETYPECODE_BL("BL", "BL - Cardiac"),
        SERVICETYPECODE_BM("BM", "BM - Lymphatic"),
        SERVICETYPECODE_BN("BN", "BN - Gastrointestinal"),
        SERVICETYPECODE_BP("BP", "BP - Endocrine"),
        SERVICETYPECODE_BQ("BQ", "BQ - Neurology"),
        SERVICETYPECODE_BR("BR", "BR - Eye"),
        SERVICETYPECODE_BS("BS", "BS - Invasive Procedures"),
        SERVICETYPECODE_BT("BT", "BT - Gynecological"),
        SERVICETYPECODE_BU("BU", "BU - Obstetrical"),
        SERVICETYPECODE_BV("BV", "BV - Obstetrical/Gynecological"),
        SERVICETYPECODE_BW("BW", "BW - Mail Order Prescription Drug: Brand Name"),
        SERVICETYPECODE_BX("BX", "BX - Mail Order Prescription Drug: Generic"),
        SERVICETYPECODE_BY("BY", "BY - Physician Visit - Office: Sick"),
        SERVICETYPECODE_BZ("BZ", "BZ - Physician Visit - Office: Well"),
        SERVICETYPECODE_C1("C1", "C1 - Coronary Care"),
        SERVICETYPECODE_CA("CA", "CA - Private Duty Nursing - Inpatient"),
        SERVICETYPECODE_CB("CB", "CB - Private Duty Nursing - Home"),
        SERVICETYPECODE_CC("CC", "CC - Surgical Benefits - Professional (Physician)"),
        SERVICETYPECODE_CD("CD", "CD - Surgical Benefits - Facility"),
        SERVICETYPECODE_CE("CE", "CE - Mental Health Provider - Inpatient"),
        SERVICETYPECODE_CF("CF", "CF - Mental Health Provider - Outpatient"),
        SERVICETYPECODE_CG("CG", "CG - Mental Health Facility - Inpatient"),
        SERVICETYPECODE_CH("CH", "CH - Mental Health Facility - Outpatient"),
        SERVICETYPECODE_CI("CI", "CI - Substance Abuse Facility - Inpatient"),
        SERVICETYPECODE_CJ("CJ", "CJ - Substance Abuse Facility - Outpatient"),
        SERVICETYPECODE_CK("CK", "CK - Screening X-ray"),
        SERVICETYPECODE_CL("CL", "CL - Screening laboratory"),
        SERVICETYPECODE_CM("CM", "CM - Mammogram, High Risk Patient"),
        SERVICETYPECODE_CN("CN", "CN - Mammogram, Low Risk Patient"),
        SERVICETYPECODE_CO("CO", "CO - Flu Vaccination"),
        SERVICETYPECODE_CP("CP", "CP - Eyewear and Eyewear Accessories"),
        SERVICETYPECODE_CQ("CQ", "CQ - Case Management"),
        SERVICETYPECODE_DG("DG", "DG - Dermatology"),
        SERVICETYPECODE_DM("DM", "DM - Durable Medical Equipment"),
        SERVICETYPECODE_DS("DS", "DS - Diabetic Supplies"),
        SERVICETYPECODE_GF("GF", "GF - Generic Prescription Drug - Formulary"),
        SERVICETYPECODE_GN("GN", "GN - Generic Prescription Drug - Non-Formulary"),
        SERVICETYPECODE_GY("GY", "GY - Allergy"),
        SERVICETYPECODE_IC("IC", "IC - Intensive Care"),
        SERVICETYPECODE_MH("MH", "MH - Mental Health"),
        SERVICETYPECODE_NI("NI", "NI - Neonatal Intensive Care"),
        SERVICETYPECODE_ON("ON", "ON - Oncology"),
        SERVICETYPECODE_PT("PT", "PT - Physical Therapy"),
        SERVICETYPECODE_PU("PU", "PU - Pulmonary"),
        SERVICETYPECODE_RN("RN", "RN - Renal"),
        SERVICETYPECODE_RT("RT", "RT - Residential Psychiatric Treatment"),
        SERVICETYPECODE_TC("TC", "TC - Transitional Care"),
        SERVICETYPECODE_TN("TN", "TN - Transitional Nursery Care"),
        SERVICETYPECODE_UC("UC", "UC - Urgent Care");

        private static final Map<String, ServiceTypeCode> descriptionMap;
        static 
        {
        	descriptionMap = new HashMap<String, ServiceTypeCode>();
            for (ServiceTypeCode v : ServiceTypeCode.values()) 
            {
            	descriptionMap.put(v.getDescription(), v);
            }
        }
        
        public static ServiceTypeCode getByDescription(String description)
        {
        	return descriptionMap.get(description);
        }
    	
        private String code;
        private String description;

        private ServiceTypeCode(String code, String description)
        {
            this.code = code;
            this.description = description;
        }

        public String getDescription()
        {
            return this.description;
        }

        public String getCode()
        {
            return this.code;
        }
        
        
    }
    
}
