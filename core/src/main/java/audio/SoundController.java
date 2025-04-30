package audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import static utilities.constants.AudiConstants.*;

public class SoundController {

    private static Map<String, Sound> sounds;
    private static Map<String, Long> currentSounds;
    private static Map<String, Float> volumeSounds;

    private static float masterVolume = 1.0f;
    private static boolean soundOn = true;



    public SoundController(){
        sounds = new HashMap<>();
        currentSounds = new HashMap<>();
        volumeSounds = new HashMap<>();
    }


    /**
     *
     * @param filePath the filePath of the sound to generate
     * @return Sound
     */
    public Sound generateSoundFromFile(String filePath){
        return Gdx.audio.newSound(Gdx.files.internal(filePath));
    }

    /**
     *
     * @param id The id of the sound
     * @param sound The sound object to use
     */
    public void addNewSound(String id, Sound sound){
        sounds.put(id, sound);
    }


    // NOTE : Si le son est déjà en cours de lecture, il ne sera pas rejoué.

    /**
     *
     * Joue un son simple (non-looping) s'il n'est pas déjà en cours de lecture.
     *
     * Si le son est déjà en cours de lecture, il ne sera pas rejoué.
     * Pour le relancer, il faut d'abord appeler {@link #stopSound(String)}.
     *
     * @param id the id of the sound
     * @return the id's sound in the memory , we can control our sound with this id (long)
     */
    public long playSound(String id){
        if(!sounds.containsKey(id)){
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
            return -1;
        } else if (!currentSounds.containsKey(id)) {
            float volume = DEFAULT_SOUND_VOLUME;
            if(volumeSounds.containsKey(id)){
                volume = volumeSounds.get(id);
            }
            long idSound = sounds.get(id).play(volume);
            currentSounds.put(id, idSound);
            volumeSounds.put(id, volume);
            return idSound;

        }else {
            Gdx.app.debug("SoundController", "Sound with Id " + id + " is already playing!");
            return currentSounds.get(id);
        }
    }


    /**
     *
     * Joue un son avec un volume personnalisé s'il n'est pas déjà en lecture.
     *
     * Si le son est déjà en cours de lecture, il ne sera pas rejoué.
     * Pour le relancer, utilisez {@link #stopSound(String)} avant d'appeler cette méthode.
     * @param id : The id of the Sound
     * @param volume : The fixed value of volume
     * @return the id's sound in the memory , we can control our sound with this id (long)
     */
    public long playSound(String id, float volume){
        if(!sounds.containsKey(id)){
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
            return -1;
        } else if (!currentSounds.containsKey(id)) {
            volumeSounds.put(id, Math.clamp(volume, MIN_SOUND_VOLUME, MAX_SOUND_VOLUME));
            long idSound = sounds.get(id).play(volumeSounds.get(id));
            currentSounds.put(id, idSound);
            return idSound;
        }else{
            Gdx.app.debug("SoundController", "Sound with Id " + id + " is already playing!");
            return currentSounds.get(id);
        }
    }


    /**
     *
     * Joue un son en boucle (looping) si ce n’est pas déjà le cas.
     *
     * ⚠️ Si le son est déjà en cours de lecture, il ne sera pas rejoué.
     * Pour forcer la relance, utilisez {@link #stopSound(String)} au préalable.
     * @param id
     * @param looping
     * @return the id's sound in the memory , we can control our sound with this id (long)
     */
    public long playSound(String id, boolean looping){
        if(!sounds.containsKey(id)){
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
            return -1;
        } else if (!currentSounds.containsKey(id)) {
            float volume = DEFAULT_SOUND_VOLUME;
            if(!volumeSounds.containsKey(id)){
                volumeSounds.put(id, volume);
            }else{
                volume = volumeSounds.get(id);
            }
            long idSound = sounds.get(id).play(volume);
            sounds.get(id).setLooping(idSound, looping);
            currentSounds.put(id, idSound);
            return idSound;
        }else {
            Gdx.app.debug("SoundController", "Sound with Id " + id + " is already playing!");
            return currentSounds.get(id);
        }
    }


    /**
     *
     * Joue un son avec volume personnalisé et option looping, s'il n'est pas déjà en lecture.
     *
     * Pour relancer le son même s’il est déjà joué, appelez {@link #stopSound(String)} d’abord.
     *
     * @param id
     * @param volume
     * @param looping
     * @return the id's sound in the memory , we can control our sound with this id (long)
     */
    public long playSound(String id, float volume, boolean looping){
        if(!sounds.containsKey(id)){
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
            return -1;
        }else if (!currentSounds.containsKey(id)) {
            float newVolume = Math.clamp(volume, MIN_SOUND_VOLUME, MAX_SOUND_VOLUME);
            long idSound = sounds.get(id).play(newVolume);
            sounds.get(id).setLooping(idSound, looping);
            currentSounds.put(id, idSound);
            volumeSounds.put(id, newVolume);
            return idSound;
        }else {
            Gdx.app.debug("SoundController", "Sound with Id " + id + " is already playing!");
            return currentSounds.get(id);
        }
    }


    public long playSound(String id, float volume, boolean looping, float pitch){
        if(!sounds.containsKey(id)){
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
            return -1;
        }else if (!currentSounds.containsKey(id)) {
            float newVolume = Math.clamp(volume, MIN_SOUND_VOLUME, MAX_SOUND_VOLUME);
            long idSound = sounds.get(id).play(newVolume);

            float newPitch = Math.clamp(pitch, 0.5f, 2.0f);

            System.out.println("rand "+newPitch);
            sounds.get(id).setPitch(idSound, newPitch);   // increases the pitch to 2x the original pitch

            sounds.get(id).setLooping(idSound, looping);
            currentSounds.put(id, idSound);
            volumeSounds.put(id, newVolume);
            return idSound;
        }else {
            Gdx.app.debug("SoundController", "Sound with Id " + id + " is already playing!");
            return currentSounds.get(id);
        }
    }

    /**
     * Calcule un volume en fonction de la distance entre la source et le listener
     * @param distance La distance actuelle
     * @param maxDistance La distance maximale d'audibilité
     * @return Un volume entre 0.0 (inaudible) et 1.0 (proche)
     */
    private float calcVolDistance(float distance, float maxDistance) {
        return MathUtils.clamp(1.0f - (distance / maxDistance), 0f, 1f);
    }


    /**
     * Calcule la position stéréo (gauche/droite) en fonction de la position relative
     * @param directionX La position horizontale relative entre la source et le listener
     * @param maxDistance La distance maximale considérée pour le pan
     * @return Une valeur de pan entre -1 (gauche) et 1 (droite)
     */
    private float calcPan(float directionX, float maxDistance) {
        return MathUtils.clamp(directionX / (maxDistance * 0.5f), -1f, 1f);
    }



    /**
     *Joue un son avec un effet de positionnement 2D (gauche/droite)
     * @param id L'identifiant du son
     * @param position La position 2D de la source sonore
     * @param listenerPosition La position 2D de l'écouteur (généralement le joueur)
     * @param maxDistance La distance maximale d'audibilité
     * @return l'identifiant du son en mémoire
     */
    public long playSound2D(String id, Vector2 position, Vector2 listenerPosition, float maxDistance) {
        if (!soundOn) {
            return -1;
        }

        if (!sounds.containsKey(id)) {
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
            return -1;
        }

        // Calculer la distance entre la source et l'écouteur
        float distance = position.dst(listenerPosition);

        // Calculer le volume en fonction de la distance
        float volume = 1.0f - MathUtils.clamp(distance / maxDistance, 0f, 1f);

        // Calculer le pan (position stéréo gauche/droite)
        // Si la source est à gauche de l'écouteur, le pan sera négatif
        float directionX = position.x - listenerPosition.x;
        float pan = MathUtils.clamp(directionX / (maxDistance * 0.5f), -1f, 1f);

        // Appliquer le volume et le pan
        float baseVolume = volumeSounds.containsKey(id) ? volumeSounds.get(id) : DEFAULT_SOUND_VOLUME;
        float finalVolume = baseVolume * volume * masterVolume;

        if (finalVolume <= 0.01f) {
            return -1; // Son trop faible pour être entendu
        }

        long soundId = sounds.get(id).play(finalVolume);
        sounds.get(id).setPan(soundId, pan, finalVolume);
        currentSounds.put(id, soundId);

        return soundId;
    }


    /**
     *
     * @param id the id of the Sound to stop
     */
    public void stopSound(String id){
        if(!sounds.containsKey(id)){
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
        } else if (!currentSounds.containsKey(id)) {
            Gdx.app.debug("SoundController", "Sound with Id" + id + " is already stopped");
        }else {
            long idSound = currentSounds.get(id);
            sounds.get(id).stop(idSound);
            currentSounds.remove(id);
        }
    }

    public void stopAllSounds(){
        for(Map.Entry<String, Long> entry : currentSounds.entrySet()){
            sounds.get(entry.getKey()).stop(entry.getValue());
        }
        currentSounds.clear();
    }

    /**
     * Récupère le volume d'un son spécifique
     * @param id L'identifiant du son
     * @return Le volume du son ou -1 si le son n'existe pas
     */
    public float getVolume(String id){
        if(!sounds.containsKey(id)) {
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
            return -1;
        } else if (!volumeSounds.containsKey(id)) {
            return DEFAULT_SOUND_VOLUME;
        } else {
            return volumeSounds.get(id);
        }
    }



    public void pauseSound(String id){
        if(!sounds.containsKey(id)) {
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
        } else if (!currentSounds.containsKey(id)) {
            Gdx.app.debug("SoundController", "Sound with Id" + id + " is not active!");
        }else{
            sounds.get(id).pause(currentSounds.get(id));
        }
    }

    /**
     * Reprend la lecture d'un son mis en pause
     * @param id L'identifiant du son à reprendre
     */
    public void resumeSound(String id) {
        if (!sounds.containsKey(id)) {
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
        } else if (!currentSounds.containsKey(id)) {
            Gdx.app.debug("SoundController", "Sound with Id " + id + " is not active!");
        } else {
            sounds.get(id).resume(currentSounds.get(id));
        }
    }


    /**
     * Définit le volume d'un son spécifique
     * @param id L'identifiant du son
     * @param volume Le nouveau volume à appliquer
     * @return Le volume final après application des limites
     */
    public float setSoundVolume(String id, float volume) {
        if (!sounds.containsKey(id)) {
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
            return -1;
        }

        float clampedVolume = MathUtils.clamp(volume, MIN_SOUND_VOLUME, MAX_SOUND_VOLUME);
        volumeSounds.put(id, clampedVolume);

        if (currentSounds.containsKey(id)) {
            sounds.get(id).setVolume(currentSounds.get(id), clampedVolume * masterVolume);
        }

        return clampedVolume;
    }



    /**
     * Augmente ou diminue le volume d'un son progressivement
     * @param id L'identifiant du son
     * @param amount La quantité à ajouter ou soustraire au volume actuel
     * @return Le nouveau volume après ajustement
     */
    public float changeVolume(String id, float amount) {
        if (!sounds.containsKey(id)) {
            Gdx.app.error("SoundController", "Sound with Id " + id + " not found!");
            return -1;
        }

        float currentVolume = volumeSounds.containsKey(id) ? volumeSounds.get(id) : DEFAULT_SOUND_VOLUME;
        float newVolume = MathUtils.clamp(currentVolume + amount, MIN_SOUND_VOLUME, MAX_SOUND_VOLUME);

        volumeSounds.put(id, newVolume);

        if (currentSounds.containsKey(id)) {
            sounds.get(id).setVolume(currentSounds.get(id), newVolume * masterVolume);
        }

        return newVolume;
    }


    /**
     * Définit le volume principal qui affecte tous les sons
     * @param volume Le nouveau volume principal
     */
    public void setMasterVolume(float volume) {
        masterVolume = MathUtils.clamp(volume, 0f, 1f);

        // Mettre à jour tous les sons en cours de lecture
        for (Map.Entry<String, Long> entry : currentSounds.entrySet()) {
            String id = entry.getKey();
            long soundId = entry.getValue();
            float soundVolume = volumeSounds.get(id);
            sounds.get(id).setVolume(soundId, soundVolume * masterVolume);
        }
    }


    /**
     * Obtient le volume principal actuel
     * @return Le volume principal
     */
    public float getMasterVolume() {
        return masterVolume;
    }


    /**
     * Active ou désactive tous les sons
     * @param enabled true pour activer les sons, false pour les désactiver
     */
    public void setSoundOn(boolean enabled) {
        if (soundOn == enabled) {
            return;
        }

        soundOn = enabled;

        if (!enabled) {
            stopAllSounds();
        }
    }



    /**
     * Indique si les sons sont activés
     * @return true si les sons sont activés, false sinon
     */
    public boolean isSoundOn() {
        return soundOn;
    }

    /**
     * Vérifie si un son est en cours de lecture
     * @param id L'identifiant du son
     * @return true si le son est en cours de lecture, false sinon
     */
    public boolean isSoundPlaying(String id) {
        return currentSounds.containsKey(id);
    }


    public void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
        currentSounds.clear();
    }

}
