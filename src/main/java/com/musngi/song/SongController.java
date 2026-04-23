package com.musngi.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/musngi/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    // HEALTH CHECK / ROOT ENDPOINT
    // This allows Render to see the app is "Live"
    @GetMapping("/")
    public String healthCheck() {
        return "Song API is running!";
    }

    // GET: Retrieve all songs
    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    // POST: Add a new song
    @PostMapping
    public Song addSong(@RequestBody Song song) {
        return songRepository.save(song);
    }

    // GET: Retrieve a specific song by ID
    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Optional<Song> song = songRepository.findById(id);
        return song.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT: Update an existing song
    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        Optional<Song> optionalSong = songRepository.findById(id);

        if (optionalSong.isPresent()) {
            Song existingSong = optionalSong.get();
            existingSong.setTitle(songDetails.getTitle());
            existingSong.setArtist(songDetails.getArtist());
            existingSong.setAlbum(songDetails.getAlbum());
            existingSong.setGenre(songDetails.getGenre());
            existingSong.setUrl(songDetails.getUrl());

            Song updatedSong = songRepository.save(existingSong);
            return ResponseEntity.ok(updatedSong);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Delete a song by ID
    @DeleteMapping("/{id}")
    public String deleteSong(@PathVariable Long id) {
        songRepository.deleteById(id);
        return "Song with ID " + id + " deleted.";
    }

    // GET: Search across title, artist, album, and genre
    @GetMapping("/search/{query}")
    public List<Song> searchSongs(@PathVariable String query) {
        return songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCaseOrGenreContainingIgnoreCase(
                query, query, query, query
        );
    }
}