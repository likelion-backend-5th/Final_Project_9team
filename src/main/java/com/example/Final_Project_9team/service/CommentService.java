package com.example.Final_Project_9team.service;

import com.example.Final_Project_9team.dto.CommentRequestDto;
import com.example.Final_Project_9team.entity.Board;
import com.example.Final_Project_9team.entity.Comment;
import com.example.Final_Project_9team.entity.User;
import com.example.Final_Project_9team.exception.CustomException;
import com.example.Final_Project_9team.exception.ErrorCode;
import com.example.Final_Project_9team.repository.BoardRepository;
import com.example.Final_Project_9team.repository.CommentRepository;
import com.example.Final_Project_9team.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public void create(String email, Long boardId, CommentRequestDto dto) {
        User writer = userRepository.findByEmail(email).get();

        Comment newComment = Comment.builder()
                .content(dto.getContent())
                .user(writer)
                .isDeleted(false)
                .build();
        commentRepository.save(newComment);
    }

    public void update(String email, Long boardId, Long commentId, CommentRequestDto dto) {
        User writer = userRepository.findByEmail(email).get();

        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_NOT_FOUND));

        if (!writer.equals(comment.getUser())) {
            throw new CustomException(ErrorCode.USER_NO_AUTH);
        }

        comment.updateContent(dto.getContent());
        commentRepository.save(comment);
    }

    public void delete(String email, Long boardId, Long commentId) {
        User writer = userRepository.findByEmail(email).get();

        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_NOT_FOUND));

        if (!writer.equals(comment.getUser())) {
            throw new CustomException(ErrorCode.USER_NO_AUTH);
        }
        comment.delete();
        commentRepository.save(comment);
    }
}