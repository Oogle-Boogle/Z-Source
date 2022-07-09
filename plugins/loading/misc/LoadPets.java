package loading.misc;

/*import com.catalyst.LoadingEvent;
import com.catalyst.world.content.pet.Pet;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import lombok.extern.log4j.Log4j;

@Log4j
public class LoadPets extends LoadingEvent {
    @Override
    protected void run() {
        new FastClasspathScanner().matchSubclassesOf(Pet.class, clazz -> {
            try {
                Pet pet = clazz.newInstance();
                Pet.pets.add(pet);
            } catch(Exception e) {
                log.error(e);
            }
        }).scan();
        log.info(Pet.pets.size() + " pets have been loaded.");
    }
}
*/