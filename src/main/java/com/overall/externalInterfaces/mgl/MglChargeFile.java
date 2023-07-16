//package com.overall.externalInterfaces.mgl;
//
//import java.sql.Date;
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class MglChargeFile
//{
//    public static class Builder
//    {
//        private List<MglChargeFileRecord> records = new LinkedList<>();
//
//        public MglChargeFileRecord.Builder accnId(String accnId)
//        {
//            Consumer<MglChargeFileRecord> f = obj -> { records.add(obj); };
//            return new MglChargeFileRecord.Builder(accnId, this, f);
//        }
//
//        public MglChargeFile build()
//        {
//            return new MglChargeFile(this);
//        }
//    }
//
//    private static final String RECORD_DELIMITER = "\n";
//
//    private final List<MglChargeFileRecord> records;
//
//    private MglChargeFile(Builder builder)
//    {
//        this.records = Collections.unmodifiableList(builder.records);
//    }
//
//    @Override
//    public String toString()
//    {
//        final StringBuilder sb = new StringBuilder();
//        for (MglChargeFileRecord record : records)
//        {
//            sb.append(record).append(RECORD_DELIMITER);
//        }
//        return sb.toString();
//    }
//
//    public static void main(String[] args)
//    {
//        MglChargeFile mglFile = new MglChargeFile.Builder()
//                .accnId("123000000")
//                    .clientId("X56")
//                    .patientDateOfBirth(new Date(System.currentTimeMillis()-31540000000L))
//                    .patientName("Aric", "Aversa", null)
//                    .patientGender("M")
//                    .dateOfService(new Date(System.currentTimeMillis()))
//                .accnId("YY000000")
//                    .patientDateOfBirth(new Date(System.currentTimeMillis()-11540000000L))
//                    .patientName("Aric", "Leone", "Midge")
//                    .clientId("P01110")
//                    .patientGender("F")
//                .build();
//
//        System.out.println(mglFile);
//    }
//}
