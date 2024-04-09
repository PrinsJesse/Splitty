package server.api;

import commons.Participant;
import commons.Tag;
import commons.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/getAll/{eventId}")
    public ResponseEntity<List<Tag>> getByEventId(@PathVariable int eventId) {
        List<Tag> tags = tagService.getTagsByEventId(eventId);
        if (tags == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<Tag> saveTag(@RequestBody TagDTO tag) {
        Tag ret = tagService.saveTag(tag);
        if (ret == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ret);
    }
    @DeleteMapping("/{name}/{eventId}")
    public ResponseEntity<Tag> deleteTag(@PathVariable String name, @PathVariable int eventId) {
        Tag tag = tagService.deleteTag(name, eventId);
        if (tag == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tag);
    }

    @PutMapping("/{name}/{eventId}")
    public ResponseEntity<Tag> updateTag(@PathVariable String name,
                                                         @PathVariable int eventId,
                                                         @RequestBody TagDTO tagDTO) {
        Tag tag = tagService.updateTag(tagDTO, name, eventId);
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tag);
    }
}
