//package com.overall.externalInterfaces.mgl;
//
//import java.sql.Date;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class MglChargeFileRecord
//{
//    public static class Builder
//    {
//        private final MglChargeFile.Builder fileBuilder;
//        private final Consumer<MglChargeFileRecord> callback;
//
//        private String accnId;
//        private Date dateOfService;
//        private String patientLastName;
//        private String patientFirstName;
//        private String patientMiddleName;
//        private Date patientDateOfBirth;
//        private String patientGender;
//        private String clientId;
//
//        public Builder(String accnId, MglChargeFile.Builder fileBuilder, Consumer<MglChargeFileRecord> callback)
//        {
//            this.accnId = accnId;
//            this.fileBuilder = fileBuilder;
//            this.callback = callback;
//        }
//
//        public Builder dateOfService(Date dateOfService)
//        {
//            this.dateOfService = dateOfService;
//            return this;
//        }
//
//        public Builder patientName(String firstName, String lastName, String middleName)
//        {
//            this.patientFirstName = firstName;
//            this.patientLastName = lastName;
//            this.patientMiddleName = middleName;
//            return this;
//        }
//
//        public Builder patientDateOfBirth(Date patientDateOfBirth)
//        {
//            this.patientDateOfBirth = patientDateOfBirth;
//            return this;
//        }
//
//        public Builder patientGender(String gender)
//        {
//            this.patientGender = gender;
//            return this;
//        }
//
//        public Builder clientId(String clientId)
//        {
//            this.clientId = clientId;
//            return this;
//        }
//
//        public MglChargeFileRecord.Builder accnId(String accnId)
//        {
//            callback.accept(new MglChargeFileRecord(this));
//            return fileBuilder.accnId(accnId);
//        }
//
//        public MglChargeFile build()
//        {
//            callback.accept(new MglChargeFileRecord(this));
//            return fileBuilder.build();
//        }
//    }
//
//    private final List<MglChargeFileField> fields;
//
//    private MglChargeFileRecord(Builder builder)
//    {
//        fields = new ArrayList<>();
//        fields.add(new MglChargeFileStringField(10, builder.accnId));
//        fields.add(new MglChargeFileDateField(14, builder.dateOfService));
//        fields.add(new MglChargeFileNameField(40, builder.patientFirstName, builder.patientLastName, builder.patientMiddleName));
//        fields.add(new MglChargeFileDateField(10, builder.patientDateOfBirth));
//        fields.add(new MglChargeFileStringField(1, builder.patientGender));
//        fields.add(new MglChargeFileStringField(6, builder.clientId));
//    }
//
//    @Override
//    public String toString()
//    {
//        StringBuilder sb = new StringBuilder();
//        for (MglChargeFileField field : fields)
//        {
//            sb.append(field);
//        }
//        return sb.toString();
//    }
//}
