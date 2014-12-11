class Answer < ActiveRecord::Base

	belongs_to :question
 	has_one     :right_answer, class_name: 'Answer',foreign_key: 'right_answer_id',dependent: :destroy

	
end
