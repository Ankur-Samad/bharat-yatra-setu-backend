package bharat_yatra_setu_backend.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import bharat_yatra_setu_backend.entity.Experience;
import bharat_yatra_setu_backend.repository.ExperienceRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ExperienceRepository experienceRepository;

    public DataInitializer(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    @Override
    public void run(String... args) {

        if (experienceRepository.count() > 0) {
            return;
        }

        Experience experience1 = new Experience();

        experience1.setTitle("Old Jaipur Culture Walk");
        experience1.setLocation("Jaipur");
        experience1.setCity("Jaipur");
        experience1.setCategory("Heritage & Culture");
        experience1.setDuration("2 hours");
        experience1.setPrice(650.0);
        experience1.setRating(4.8);
        experience1.setReviewCount(124);
        experience1.setTrustScore(96);
        experience1.setProvider("Priya Sharma");
        experience1.setProviderType("Local Guide");
        experience1.setLanguages("Hindi, English");
        experience1.setMaxGuests(6);
        experience1.setMeetingPoint("Hawa Mahal, Jaipur");
        experience1.setDescription(
                "Explore the hidden lanes, local stories, traditional markets and heritage culture of Old Jaipur with a verified local guide.");
        experience1.setImage(
                "https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=1200&q=80");

        Experience experience2 = new Experience();

        experience2.setTitle("Amer Fort Heritage Tour");
        experience2.setLocation("Jaipur");
        experience2.setCity("Jaipur");
        experience2.setCategory("Heritage & Culture");
        experience2.setDuration("2 hours");
        experience2.setPrice(800.0);
        experience2.setRating(4.9);
        experience2.setReviewCount(186);
        experience2.setTrustScore(98);
        experience2.setProvider("Ravi Sharma");
        experience2.setProviderType("Local Heritage Guide");
        experience2.setLanguages("Hindi, English");
        experience2.setMaxGuests(6);
        experience2.setMeetingPoint("Amer Fort Main Gate");
        experience2.setDescription(
                "Discover the history, architecture and stories of Amer Fort with a trusted local heritage guide.");
        experience2.setImage(
                "https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=1200&q=80");

        Experience experience3 = new Experience();

        experience3.setTitle("Jaipur Local Food Experience");
        experience3.setLocation("Jaipur");
        experience3.setCity("Jaipur");
        experience3.setCategory("Food & Culture");
        experience3.setDuration("2 hours");
        experience3.setPrice(500.0);
        experience3.setRating(4.7);
        experience3.setReviewCount(98);
        experience3.setTrustScore(94);
        experience3.setProvider("Saira Khan");
        experience3.setProviderType("Local Food Host");
        experience3.setLanguages("Hindi, English");
        experience3.setMaxGuests(5);
        experience3.setMeetingPoint("Masala Chowk, Jaipur");
        experience3.setDescription(
                "Taste authentic Jaipur street food and discover the stories behind Rajasthan's local flavours with a local host.");
        experience3.setImage(
                "https://images.unsplash.com/photo-1601050690597-df0568f70950?auto=format&fit=crop&w=1200&q=80");

        experienceRepository.save(experience1);
        experienceRepository.save(experience2);
        experienceRepository.save(experience3);

        System.out.println("✅ 3 experiences inserted successfully!");
    }
}