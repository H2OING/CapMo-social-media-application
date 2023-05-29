package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.application.data.entity.Likes;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.UserPost;

class UserPostTest {

    @Mock
    private Profile mockProfile;

    private UserPost userPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userPost = new UserPost("Description", 0, null, mockProfile);
    }

    @Test
    void NumberOfLikedPostsTest() {
        Likes mockLike1 = mock(Likes.class);
        Likes mockLike2 = mock(Likes.class);
        Likes mockLike3 = mock(Likes.class);

        Collection<Likes> likes = new ArrayList<>();
        likes.add(mockLike1);
        likes.add(mockLike2);
        likes.add(mockLike3);
        userPost.setLike(likes);

        when(mockLike1.isLiked()).thenReturn(true);
        when(mockLike2.isLiked()).thenReturn(true);
        when(mockLike3.isLiked()).thenReturn(false);

        int likedPosts = userPost.likes();
        assertEquals(2, likedPosts);
    }

    @Test
    void NoLikedPostsTest() {
        Collection<Likes> likes = new ArrayList<>();
        userPost.setLike(likes);

        int likedPosts = userPost.likes();
        assertEquals(0, likedPosts);
    }
}
