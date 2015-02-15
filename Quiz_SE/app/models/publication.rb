class Publication < ActiveRecord::Base

	belongs_to :quiz, foreign_key: 'quiz_id'
	belongs_to :group, foreign_key: 'group_id'
	has_many :students_quizzes

end