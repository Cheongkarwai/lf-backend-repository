package com.lfhardware.appointment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_appointment_image")
public class AppointmentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_image_id_seq_generator")
    @SequenceGenerator(name = "appointment_image_id_seq_generator", sequenceName = "appointment_image_seq", allocationSize = 1)
    private Long id;

    private String path;

    @ManyToOne
    @PrimaryKeyJoinColumns({
            @PrimaryKeyJoinColumn(name = "service_id", referencedColumnName = "serviceId"),
            @PrimaryKeyJoinColumn(name = "service_provider_id", referencedColumnName = "serviceProviderId"),
            @PrimaryKeyJoinColumn(name = "customer_id", referencedColumnName = "customerId"),
            @PrimaryKeyJoinColumn(name = "created_at", referencedColumnName = "createdAt")
    })
    private Appointment appointment;

//    public void setAppointment(Appointment appointment){
//        this.appointment = appointment;
////        appointment.getAppointmentImages().add(this);
//    }

}
