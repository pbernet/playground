//Function composition according to
//http://danielwestheide.com/blog/2013/01/23/the-neophytes-guide-to-scala-part-10-staying-dry-with-higher-order-functions.html

case class Email(
                  subject: String,
                  text: String,
                  sender: String,
                  recipient: String)
type EmailFilter = Email => Boolean

def newMailsForUser(mails: Seq[Email], filter: EmailFilter) = mails.filter(filter)

val sentByOneOf: Set[String] => EmailFilter =
  senders => email => senders.contains(email.sender)

val notSentByAnyOf: Set[String] => EmailFilter =
  (senders: Set[String]) => (email: Email) => !senders.contains(email.sender)


type SizeChecker = Int => Boolean
val sizeConstraint: SizeChecker => EmailFilter = { (f: SizeChecker) => (email: Email) => f(email.text.size) }


val minimumSize: Int => EmailFilter = n => sizeConstraint(_ >= n)
val maximumSize: Int => EmailFilter = n => sizeConstraint(_ <= n)


val emailFilter: EmailFilter = notSentByAnyOf(Set("johndoe@example.com"))
val mails = Email(
  subject = "It's me again, your stalker friend!",
  text = "Hello my friend! How are you?",
  sender = "johndoe@example.com",
  recipient = "me@example.com") :: Nil
newMailsForUser(mails, emailFilter) // returns an empty list










