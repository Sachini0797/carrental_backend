package com.sachini.booking.model;

import com.sachini.booking.enums.FuelType;
import com.sachini.booking.enums.VehicleStatus;
import com.sachini.booking.enums.VehicleTransmission;
import com.sachini.booking.enums.VehicleTypeEnum;

import javax.persistence.*;

@Entity
@Table(name = "VEHICLE")
public class Vehicle {

    @Id
    @Column(name = "vehicle_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vehicleId;

    @Column(name = "vehicle_name")
    private String vehicleName;

    @Column(name = "fuel_type")
    private FuelType fuelType;

    @Column(name = "vehicle_doors")
    private int vehicleDoors;

    @Column(name = "vehicle_seats")
    private int vehicleSeats;

    @Column(name = "transmission")
    private VehicleTransmission transmission;
    
    @Column(name = "rate_per_month")
    private double ratePerMonth;
    
    @Column(name = "rate_per_week")
    private double ratePerWeek;
    

    @Column(name = "price_per_day")
    private double pricePerDay;

    @Column(name = "vehicleImage")
    private String vehicleImage;

    @Column(name = "vehicle_status")
    private VehicleStatus vehicleStatus;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", nullable = false)
    private VehicleType vehicleType;

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public int getVehicleDoors() {
        return vehicleDoors;
    }

    public void setVehicleDoors(int vehicleDoors) {
        this.vehicleDoors = vehicleDoors;
    }

    public int getVehicleSeats() {
        return vehicleSeats;
    }

    public void setVehicleSeats(int vehicleSeats) {
        this.vehicleSeats = vehicleSeats;
    }

    public VehicleTransmission getTransmission() {
        return transmission;
    }

    public void setTransmission(VehicleTransmission transmission) {
        this.transmission = transmission;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public VehicleStatus getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(VehicleStatus vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public double getRatePerMonth() {
		return ratePerMonth;
	}

	public void setRatePerMonth(double ratePerMonth) {
		this.ratePerMonth = ratePerMonth;
	}

	public double getRatePerWeek() {
		return ratePerWeek;
	}

	public void setRatePerWeek(double ratePerWeek) {
		this.ratePerWeek = ratePerWeek;
	}
	
	

	
}
