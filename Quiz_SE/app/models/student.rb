class Student < User

    has_many :memberships ,foreign_key: 'student_id'
    has_many :groups, through: :memberships
    has_and_belongs_to_many :answers, join_table: :students_answers, foreign_key: :student_id
    has_many :students_quizzes
    has_many :quizzes, through: :students_quizzes, foreign_key: :student_id

    def self.model_name
		User.model_name
	end

	def answer_quiz(answers_id, quiz_id)
        mark = 0
        answers_id.each do |answer_id|
            answer = Answer.find(answer_id)
            if answer.question.right_answer == answer
                mark += 1
            end
        end
        answered_quiz = Publication.find(quiz_id)
        grade = mark.to_f / answered_quiz.quiz.questions.count * answered_quiz.quiz.marks
        StudentsQuiz.create(student_id: id, quiz_id: answered_quiz.quiz.id, publication_id: quiz_id, marks: grade)
        grade
    end

end