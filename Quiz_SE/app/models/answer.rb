class Answer < ActiveRecord::Base

	#Relationships
	belongs_to :question
 	has_one     :right_answer, class_name: 'Answer',foreign_key: 'right_answer_id',dependent: :destroy
	has_and_belongs_to_many :students, join_table: :students_answers, foreign_key: :answer_id
	
	#Validations
	validates_presence_of :title

end
