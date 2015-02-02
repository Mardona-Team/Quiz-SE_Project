class Question < ActiveRecord::Base

	#Relationships
   	has_many    :answers, dependent: :destroy
   	accepts_nested_attributes_for :answers
   	belongs_to  :right_answer, class_name: 'Answer',foreign_key: 'right_answer_id'
   	belongs_to  :quiz
   	accepts_nested_attributes_for :right_answer

   	#Callbacks
   	after_create :add_right_answer_to_answers

   	#Validations
   	validates_presence_of :title

	def add_right_answer_to_answers
		self.answers << right_answer
	end
   	



end
