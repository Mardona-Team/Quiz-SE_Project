class Question < ActiveRecord::Base

  
   	has_many    :answers, dependent: :destroy
   	belongs_to  :right_answer, class_name: 'Answer',foreign_key: 'right_answer_id'
   	belongs_to  :quiz

   	
	def shuffled_answers
		self.answers.select("id, title").shuffle
	end


end
