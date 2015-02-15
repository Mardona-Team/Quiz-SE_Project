class StudentsQuiz < ActiveRecord::Base

	belongs_to :student, class_name: 'User'
	belongs_to :quiz
	belongs_to :publication

end
