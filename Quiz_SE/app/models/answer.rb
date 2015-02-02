class Answer < ActiveRecord::Base

	#Relationships
	belongs_to :question
 	has_one     :right_answer, class_name: 'Answer',foreign_key: 'right_answer_id',dependent: :destroy

	#Validations
	validates_presence_of :title

end
