package com.gitlabdemo.issuecomments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gitlab")
public class GitLabCommentController {

    @Autowired
    private GitLabCommentService gitLabCommentService;

    // Add comment with an attachment   
    @PostMapping("/issues/{issueId}/comment-attachment")
    public String addCommentAndAttachment(@PathVariable String issueId, @RequestParam String comment, @RequestParam String filePath) {
        try {
            gitLabCommentService.addCommentAndAttachment(issueId, comment, filePath);
            return "Comment and attachment added successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to add comment: " + e.getMessage();
        }
    }
}