package bharat_yatra_setu_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bharat_yatra_setu_backend.dto.ExperienceRequest;
import bharat_yatra_setu_backend.entity.Experience;
import bharat_yatra_setu_backend.exception.ResourceNotFoundException;
import bharat_yatra_setu_backend.repository.ExperienceRepository;

@Service
public class ExperienceService {

    private static final String DEFAULT_IMAGE =
            "https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=1200&q=80";

    private final ExperienceRepository experienceRepository;

    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    public List<Experience> getAllExperiences() {
        return experienceRepository.findAll();
    }

    public Experience getExperience(Long id) {
        return experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Experience not found"
                ));
    }

    @Transactional
    public Experience createExperience(ExperienceRequest request) {
        Experience experience = new Experience();
        applyRequest(experience, request, true);
        return experienceRepository.save(experience);
    }

    @Transactional
    public Experience updateExperience(Long id, ExperienceRequest request) {
        Experience experience = getExperience(id);
        applyRequest(experience, request, false);
        return experienceRepository.save(experience);
    }

    @Transactional
    public void deleteExperience(Long id) {
        Experience experience = getExperience(id);
        experienceRepository.delete(experience);
    }

    private void applyRequest(
            Experience experience,
            ExperienceRequest request,
            boolean creating
    ) {
        experience.setTitle(request.getTitle().trim());
        experience.setCategory(request.getCategory().trim());
        experience.setDescription(request.getDescription().trim());
        experience.setPrice(request.getPrice());

        String city = firstNonBlank(request.getCity(), experience.getCity(), "Jaipur");
        experience.setCity(city);
        experience.setLocation(firstNonBlank(
                request.getLocation(),
                experience.getLocation(),
                city + ", Rajasthan"
        ));
        experience.setDuration(firstNonBlank(
                request.getDuration(),
                experience.getDuration(),
                "2 hours"
        ));
        experience.setLanguages(firstNonBlank(
                request.getLanguages(),
                experience.getLanguages(),
                "Hindi, English"
        ));
        experience.setProvider(firstNonBlank(
                request.getProvider(),
                experience.getProvider(),
                "Local Provider"
        ));
        experience.setProviderType(firstNonBlank(
                request.getProviderType(),
                experience.getProviderType(),
                "Local Host"
        ));
        experience.setMeetingPoint(firstNonBlank(
                request.getMeetingPoint(),
                experience.getMeetingPoint(),
                experience.getLocation()
        ));
        experience.setImage(firstNonBlank(
                request.getImage(),
                experience.getImage(),
                DEFAULT_IMAGE
        ));

        Integer maxGuests = request.getMaxGuests();
        if (maxGuests != null) {
            experience.setMaxGuests(maxGuests);
        } else if (creating || experience.getMaxGuests() == null) {
            experience.setMaxGuests(6);
        }

        if (request.getRating() != null) {
            experience.setRating(request.getRating());
        } else if (creating || experience.getRating() == null) {
            experience.setRating(4.5);
        }

        if (request.getReviewCount() != null) {
            experience.setReviewCount(request.getReviewCount());
        } else if (creating || experience.getReviewCount() == null) {
            experience.setReviewCount(0);
        }

        if (request.getTrustScore() != null) {
            experience.setTrustScore(request.getTrustScore());
        } else if (creating || experience.getTrustScore() == null) {
            experience.setTrustScore(88);
        }
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }

        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        return null;
    }
}
