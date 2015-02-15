class User < ActiveRecord::Base

  before_save :ensure_authentication_token

  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :registerable,
       :recoverable, :rememberable, :trackable, :validatable,
       :token_authenticatable,:confirmable


  USERNAME_REGEX = /[a-zA-Z](([\._\-][a-zA-Z0-9])|[a-zA-Z0-9])*[a-z0-9]/
  NAME_REGEX = /[a-zA-Z]/

	validates :username,
	presence: true,
	length: { minimum: 5, maximum: 25},
	uniqueness: true,
	format: USERNAME_REGEX


	validates :first_name, presence: true,format: NAME_REGEX
	validates :last_name, presence: true,format: NAME_REGEX
	validates :type, presence: true




  #A method used to bypass confirmation step after creating a new userx
  def skip_confirmation!
    self.confirmed_at = Time.now
  end

  def full_name
    first_name + " " + last_name
  end

end



class Student < User

    has_many    :memberships ,foreign_key: 'student_id'
    has_many :groups, through: :memberships
    has_and_belongs_to_many :answers, join_table: :students_answers, foreign_key: :student_id
    has_many :students_quizzes
    has_many :quizzes, through: :students_quizzes, foreign_key: :student_id

    def self.model_name
		User.model_name
	end

	def answer_quiz(answers_id, quiz_id)
        mark = 0
        answered_quiz = Quiz.find(quiz_id)
        answers_id.each do |answer_id|
            answer = Answer.find(answer_id)
            if answer.question.right_answer == answer
                mark += 1
            end
        end
        grade = mark.to_f / answered_quiz.questions.count * answered_quiz.marks
        StudentsQuiz.create(student_id: id, quiz_id: quiz_id, marks: grade)
        grade
    end

end


class Instructor < User

	has_many    :groups, dependent: :destroy
    has_many    :quizzes, dependent: :destroy

    def self.model_name
		User.model_name
	end


end