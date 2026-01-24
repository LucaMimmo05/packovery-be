package com.packovery.location;

import com.packovery.location.utils.GpsUtils;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class TrackingService {

    @Inject
    RiderLocationRepository riderLocationRepository;

    @Transactional
    public void updateRiderLocation(Long riderId, Double newLatitude, Double newLongitude) {
        RiderLocation newRiderLocation = new RiderLocation();
        newRiderLocation.riderId = riderId;
        newRiderLocation.latitude = newLatitude;
        newRiderLocation.longitude = newLongitude;
        newRiderLocation.positionTimestamp = Instant.now();

        RiderLocation lastLocation = getLastPosition(riderId);

        if(lastLocation != null) {
            newRiderLocation.distanceTraveled = GpsUtils.calculateDistance(
                    lastLocation.latitude, lastLocation.longitude,
                    newLatitude, newLongitude
            );
        } else {
            newRiderLocation.distanceTraveled = 0.0;
        }
        riderLocationRepository.persist(newRiderLocation);
    }

    public RiderLocation getLastPosition(Long riderId) {
        return riderLocationRepository.find("riderId = ?1", Sort.descending("positionTimestamp"), riderId).firstResult();
    }

    public List<RiderLocation> getRouteHistory(Long riderId){
        return riderLocationRepository.list("riderId = ?1", Sort.ascending("positionTimestamp"), riderId);
    }
}
