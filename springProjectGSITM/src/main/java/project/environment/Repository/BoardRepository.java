package project.environment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.environment.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
}