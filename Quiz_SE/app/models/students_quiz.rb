class StudentsQuiz < ActiveRecord::Base

	belongs_to :student, class_name: 'User'
	belongs_to :quiz

	def answers
	end
end
